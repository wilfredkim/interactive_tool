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
        log.info("Imsi {} msisdn {} Service Code {}", imsi, msisdn, serviceCode);
        Session session = sessionRepository.findBySessionIdAndIsActive(sessionId,Boolean.TRUE).orElseGet(() -> createNewSession(sessionId, imsi, msisdn));
        Menu currentMenu = ussdMenuRepository.findById(session.getCurrentMenuId()).orElseThrow();
        if (!text.isBlank()) {
            handleUserInput(text, session, currentMenu);
            currentMenu = ussdMenuRepository.findById(session.getCurrentMenuId()).orElseThrow();
        }
        return buildResponse(currentMenu);
    }

    private void handleUserInput(String text, Session session, Menu menu) {
        switch (menu.getMenuType()) {
            case INPUT -> handleInput(text, session, menu);
            case MENU, DYNAMIC -> handleMenuSelection(text, session, menu);
        }
    }

    private void handleInput(String text, Session session, Menu menu) {

    }

    private void handleMenuSelection(String text, Session session, Menu menu) {
    }

    private String buildResponse(Menu menu) {

        StringBuilder response = new StringBuilder("CON ");
        response.append(menu.getTitle()).append("\n");
        if (menu.getMenuType() == MenuType.MENU) {
            ussdMenuRepository.findActiveByParentMenuId(menu.getId())
                    .forEach(o -> response
                            .append(o.getDisplayOrder())
                            .append(". ")
                            .append(o.getTitle())
                            .append("\n"));
        }

        if (menu.getMenuType() == MenuType.DYNAMIC) {
            //implement logic!!
            String eventId = menu.getEventId();

            switch (eventId) {
                case "GetAccounts":
                    List<String> accounts = Arrays.asList("092332323232", "02332323222332");
                    for (int i = 0; i < accounts.size(); i++) {
                        response.append(i + 1).append(". ").append(accounts.get(i)).append("\n");
                    }
                    break;
                default:
                    response.append("Unknown event: ").append(eventId).append("\n");
                    break;
            }

        }

        return response.toString().trim();
    }

    private Session createNewSession(String sessionId, String imsi, String msisdn) {
        Menu root = ussdMenuRepository.findRootMenu();
        Session session = new Session();
        session.setSessionId(sessionId);
        session.setIsActive(true);
        session.setCurrentMenuId(root.getId());
        session.setImsi(imsi);
        session.setMsisdn(msisdn);
        return sessionRepository.save(session);
    }
}
