package com.interactivetool.wilfred.interactivetool.services.impl;

import com.interactivetool.wilfred.interactivetool.entity.Session;
import com.interactivetool.wilfred.interactivetool.entity.Menu;
import com.interactivetool.wilfred.interactivetool.entity.MenuType;
import com.interactivetool.wilfred.interactivetool.repository.SessionRepository;
import com.interactivetool.wilfred.interactivetool.repository.MenuRepository;
import com.interactivetool.wilfred.interactivetool.services.UssdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UssdServiceImpl implements UssdService {
    private final MenuRepository ussdMenuRepository;
    private final SessionRepository sessionRepository;

    @Override
    public String processInput(String msisdn, String serviceCode, String sessionId, String imsi, String text) {
        log.info("Processing - MSISDN: {}, SessionID: {}, Text: {}", msisdn, sessionId, text);


        Session session = sessionRepository.findBySessionIdAndIsActive(sessionId, Boolean.TRUE)
                .orElseGet(() -> createNewSession(sessionId, imsi, msisdn));

        // Parse USSD string to get current input
        String currentInput = parseCurrentInput(text);
        log.info("Parsed Current Input: {} from USSD String: {}", currentInput, text);
        if (currentInput == null) {
            session.setIsActive(false);
            sessionRepository.save(session);
            session = createNewSession(sessionId, imsi, msisdn);
        }
        session.setUssdString(text);
        session.setCurrentInput(currentInput);

        Menu currentMenu = ussdMenuRepository.findById(session.getCurrentMenuId()).orElseThrow();


        if (currentInput != null && !currentInput.isEmpty()) {
            log.info("Current Input: {} for Session: {}", currentInput, sessionId);
            handleUserInput(currentInput, session, currentMenu);
            currentMenu = ussdMenuRepository.findById(session.getCurrentMenuId()).orElseThrow();
        }
        sessionRepository.save(session);
        return buildResponse(currentMenu, session);
    }

    private String parseCurrentInput(String ussdString) {
        if (ussdString == null || ussdString.isEmpty()) {
            return null;
        }

        // Remove service code prefix and split by *
        String[] parts = ussdString.split("\\*");

        // Return the last part as current input
        return parts[parts.length - 1];
    }

    private void handleUserInput(String input, Session session, Menu menu) {
        log.info("Handling input: {} for Menu: {} of type: {}", input, menu.getMenuCode(), menu.getMenuType());
        switch (menu.getMenuType()) {
            case INPUT -> handleInput(input, session, menu);
            case MENU, DYNAMIC -> handleMenuSelection(input, session, menu);
        }
    }

    private void handleInput(String input, Session session, Menu menu) {
        // Handle different input types based on eventId
        String eventId = menu.getEventId();
        log.info("event id::::::::::::::::::::::::::::::::::{}", eventId);

        switch (eventId) {
            case "PIN_VALIDATION" -> {
                if ("1234".equals(input)) { // Hardcoded PIN validation
                    session.setPinAttempts(0); // Reset attempts on success
                    navigateToNextMenu(session, menu);
                } else {
                    // Increment attempts
                    int attempts = session.getPinAttempts() != null ? session.getPinAttempts() : 0;
                    attempts++;
                    session.setPinAttempts(attempts);

                    if (attempts >= 3) {
                        // End session after 3 failed attempts
                        session.setIsActive(false);
                    }
                    // Stay on same menu for invalid PIN
                }
            }
            case "AMOUNT_INPUT" -> {
                if (isValidAmount(input)) {
                    session.setNavigationPath(session.getNavigationPath() + "|AMOUNT:" + input);
                    navigateToNextMenu(session, menu);
                }
            }
            case "PHONE_INPUT" -> {
                if (isValidPhoneNumber(input)) {
                    session.setNavigationPath(session.getNavigationPath() + "|PHONE:" + input);
                    navigateToNextMenu(session, menu);
                }
                // Stay on same menu for invalid phone - error message will be shown in buildResponse
            }
            default -> navigateToNextMenu(session, menu);
        }
    }

    private void handleMenuSelection(String input, Session session, Menu menu) {
        try {
            int selection = Integer.parseInt(input);

            // Find the selected menu item
            Menu selectedMenu = ussdMenuRepository.findByParentMenuIdAndDisplayOrder(menu.getId(), selection);

            if (selectedMenu != null) {
                // Handle special cases
                if (selectedMenu.getIsBack() != null && selectedMenu.getIsBack()) {
                    navigateBack(session);
                } else {
                    session.setCurrentMenuId(selectedMenu.getId());
                    // Update navigation path
                    String currentPath = session.getNavigationPath() != null ? session.getNavigationPath() : "";
                    session.setNavigationPath(currentPath + "|" + selectedMenu.getMenuCode());
                }
            } else {
                log.warn("Invalid selection {} for menu {}", selection, menu.getId());
            }
        } catch (NumberFormatException e) {
            log.warn("Invalid input format: {}", input);
        }
    }

    private void navigateToNextMenu(Session session, Menu currentMenu) {
        // For PIN validation, move to main menu
        if ("PIN_VALIDATION".equals(currentMenu.getEventId())) {
            Menu mainMenu = ussdMenuRepository.findByMenuCode("MAIN");
            if (mainMenu != null) {
                session.setCurrentMenuId(mainMenu.getId());
            }
            return;
        }

        // For other cases, find child menus
        List<Menu> childMenus = ussdMenuRepository.findByParentMenuIdAndIsActiveOrderByDisplayOrder(currentMenu.getId());
        if (!childMenus.isEmpty()) {
            session.setCurrentMenuId(childMenus.get(0).getId());
        }
    }

    private void navigateBack(Session session) {
        // Simple back navigation - you can enhance this
        String path = session.getNavigationPath();
        if (path != null && path.contains("|")) {
            String[] parts = path.split("\\|");
            if (parts.length > 1) {
                // Remove last part and find corresponding menu
                StringBuilder newPath = new StringBuilder();
                for (int i = 0; i < parts.length - 1; i++) {
                    if (i > 0) newPath.append("|");
                    newPath.append(parts[i]);
                }
                session.setNavigationPath(newPath.toString());
                // You'd need to implement logic to find menu by path
            }
        }
    }

    private boolean isValidAmount(String amount) {
        try {
            double value = Double.parseDouble(amount);
            return value > 0 && value <= 100000; // Max 100K
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.matches("^254[0-9]{9}$");
    }

    private String buildResponse(Menu menu, Session session) {
        StringBuilder response = new StringBuilder("CON ");
        response.append(menu.getTitle()).append("\n");

        StringBuilder finalResponse = response;
        switch (menu.getMenuType()) {
            case MENU -> {
                List<Menu> childMenus = ussdMenuRepository.findByParentMenuIdAndIsActiveOrderByDisplayOrder(menu.getId());
                childMenus.forEach(childMenu -> finalResponse
                        .append(childMenu.getDisplayOrder())
                        .append(". ")
                        .append(childMenu.getTitle())
                        .append("\n"));
            }
            case DYNAMIC -> {
                String eventId = menu.getEventId();
                switch (eventId) {
                    case "GetAccounts" -> {
                        List<String> accounts = Arrays.asList("23233232323232", "012342323223", "922323233232");
                        for (int i = 0; i < accounts.size(); i++) {
                            response.append(i + 1).append(". ").append(accounts.get(i)).append("\n");
                        }
                    }
                    case "CONFIRMATION" -> {
                        // Build confirmation message from session data
                        String path = session.getNavigationPath();
                        String amount = extractFromPath(path, "AMOUNT");
                        String phone = extractFromPath(path, "PHONE");
                        response.append(String.format("Send KES %s to MPESA %s\n",
                                amount != null ? amount : "0",
                                phone != null ? phone : "N/A"));
                        response.append("Total Debit Amount: KES ").append(amount != null ? amount : "0").append("\n");
                        response.append("1. Proceed\n2. Cancel\n");
                    }
                    default -> response.append("Unknown event: ").append(eventId).append("\n");
                }
            }
            case INPUT -> {
                // Handle PIN validation messages
                if ("PIN_VALIDATION".equals(menu.getEventId())) {
                    int attempts = session.getPinAttempts() != null ? session.getPinAttempts() : 0;
                    if (attempts > 0) {
                        if (attempts >= 3) {
                            return "END Invalid PIN. Maximum attempts exceeded. Session terminated.";
                        } else {
                            response = new StringBuilder("CON Invalid PIN. Attempts remaining: ").append(3 - attempts).append("\n");
                            response.append("Please enter your PIN to Login");
                        }
                    }
                }
                // Handle phone validation messages
                if ("PHONE_INPUT".equals(menu.getEventId()) && session.getCurrentInput() != null) {
                    if (!isValidPhoneNumber(session.getCurrentInput())) {
                        response = new StringBuilder("CON Invalid phone number. Please enter a valid phone number (254XXXXXXXXX):\n");
                        response.append(menu.getTitle());
                    }
                }
            }
        }

        return response.toString().trim();
    }

    private String extractFromPath(String path, String key) {
        if (path == null) return null;
        String[] parts = path.split("\\|");
        for (String part : parts) {
            if (part.startsWith(key + ":")) {
                return part.substring(key.length() + 1);
            }
        }
        return null;
    }

    private Session createNewSession(String sessionId, String imsi, String msisdn) {
        Menu root = ussdMenuRepository.findRootMenu();
        Session session = new Session();
        session.setSessionId(sessionId);
        session.setIsActive(true);
        session.setCurrentMenuId(root.getId());
        session.setImsi(imsi);
        session.setMsisdn(msisdn);
        session.setPinAttempts(0);
        return sessionRepository.save(session);
    }
}
