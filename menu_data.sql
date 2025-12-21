-- Sample menu data for USSD Interactive System
-- This demonstrates how to use one table for the entire menu structure

-- Root Menu (PIN Entry)
INSERT INTO ussd_menu (menu_code, title, parent_menu_id, display_order, is_active, is_root, is_back, menu_type, event_id) 
VALUES ('ROOT', 'Welcome to Wilfred Interactive System\nPlease enter your PIN to Login', NULL, 1, true, true, false, 'INPUT', 'PIN_VALIDATION');

-- Main Menu
INSERT INTO ussd_menu (menu_code, title, parent_menu_id, display_order, is_active, is_root, is_back, menu_type, event_id) 
VALUES ('MAIN', 'Reply With', 1, 1, true, false, false, 'MENU', NULL);

-- Main Menu Options
INSERT INTO ussd_menu (menu_code, title, parent_menu_id, display_order, is_active, is_root, is_back, menu_type, event_id) 
VALUES ('SEND_MONEY', 'Send Money', (select max(id) from ussd_menu where menu_code='MAIN' and is_active =true and menu_type ='MENU'), 1, true, false, false, 'MENU', NULL);

INSERT INTO ussd_menu (menu_code, title, parent_menu_id, display_order, is_active, is_root, is_back, menu_type, event_id) 
VALUES ('UTILITY_BILLS', 'Utility Bills', (select max(id) from ussd_menu where menu_code='MAIN' and is_active =true and menu_type ='MENU'), 2, true, false, false, 'MENU', NULL);

INSERT INTO ussd_menu (menu_code, title, parent_menu_id, display_order, is_active, is_root, is_back, menu_type, event_id) 
VALUES ('BUY_AIRTIME', 'Buy Airtime', (select max(id) from ussd_menu where menu_code='MAIN' and is_active =true and menu_type ='MENU'), 3, true, false, false, 'MENU', NULL);

INSERT INTO ussd_menu (menu_code, title, parent_menu_id, display_order, is_active, is_root, is_back, menu_type, event_id) 
VALUES ('PAYBILL', 'Paybill/Till', (select max(id) from ussd_menu where menu_code='MAIN' and is_active =true and menu_type ='MENU'), 4, true, false, false, 'MENU', NULL);

INSERT INTO ussd_menu (menu_code, title, parent_menu_id, display_order, is_active, is_root, is_back, menu_type, event_id) 
VALUES ('EXIT', 'Exit', (select max(id) from ussd_menu where menu_code='MAIN' and is_active =true and menu_type ='MENU'), 5, true, false, false, 'MENU', NULL);

-- Send Money Options
INSERT INTO ussd_menu (menu_code, title, parent_menu_id, display_order, is_active, is_root, is_back, menu_type, event_id) 
VALUES ('MPESA', 'Mpesa', (select max(id) from ussd_menu where menu_code='SEND_MONEY' and is_active =true and menu_type ='MENU'), 1, true, false, false, 'MENU', NULL);

INSERT INTO ussd_menu (menu_code, title, parent_menu_id, display_order, is_active, is_root, is_back, menu_type, event_id) 
VALUES ('AIRTEL_MONEY', 'Airtel Money', (select max(id) from ussd_menu where menu_code='SEND_MONEY' and is_active =true and menu_type ='MENU'), 2, true, false, false, 'MENU', NULL);

INSERT INTO ussd_menu (menu_code, title, parent_menu_id, display_order, is_active, is_root, is_back, menu_type, event_id) 
VALUES ('GO_BACK_1', 'Go Back', (select max(id) from ussd_menu where menu_code='SEND_MONEY' and is_active =true and menu_type ='MENU'), 3, true, false, true, 'MENU', NULL);

-- Mpesa Options
INSERT INTO ussd_menu (menu_code, title, parent_menu_id, display_order, is_active, is_root, is_back, menu_type, event_id) 
VALUES ('MY_NUMBER', 'My Number', (select max(id) from ussd_menu where menu_code='MPESA' and is_active =true and menu_type ='MENU'), 1, true, false, false, 'MENU', NULL);

INSERT INTO ussd_menu (menu_code, title, parent_menu_id, display_order, is_active, is_root, is_back, menu_type, event_id) 
VALUES ('OTHER_NUMBERS', 'Other Mobile Numbers', (select max(id) from ussd_menu where menu_code='MPESA' and is_active =true and menu_type ='MENU'), 2, true, false, false, 'MENU', NULL);

INSERT INTO ussd_menu (menu_code, title, parent_menu_id, display_order, is_active, is_root, is_back, menu_type, event_id) 
VALUES ('GO_BACK_2', 'Go Back', (select max(id) from ussd_menu where menu_code='MPESA' and is_active =true and menu_type ='MENU'), 3, true, false, true, 'MENU', NULL);

INSERT INTO ussd_menu (menu_code, title, parent_menu_id, display_order, is_active, is_root, is_back, menu_type, event_id)
VALUES ('ENTER_PHONE', 'Enter Phone Number', (select max(id) from ussd_menu where menu_code='OTHER_NUMBERS' and is_active =true and menu_type ='MENU'), 1, true, false, false, 'INPUT', 'PHONE_INPUT');


-- Account Selection (Dynamic Menu)
INSERT INTO ussd_menu (menu_code, title, parent_menu_id, display_order, is_active, is_root, is_back, menu_type, event_id) 
VALUES ('SELECT_ACCOUNT', 'Select Account to Debit', (select max(id) from ussd_menu where menu_code='ENTER_PHONE' and is_active =true), 1, true, false, false, 'DYNAMIC', 'GetAccounts');

-- Amount Input
INSERT INTO ussd_menu (menu_code, title, parent_menu_id, display_order, is_active, is_root, is_back, menu_type, event_id) 
VALUES ('ENTER_AMOUNT', 'Enter Amount', (select max(id) from ussd_menu where menu_code='SELECT_ACCOUNT' and is_active =true), 1, true, false, false, 'INPUT', 'AMOUNT_INPUT');

-- Phone Number Input (for Other Numbers option)

-- Confirmation Screen
INSERT INTO ussd_menu (menu_code, title, parent_menu_id, display_order, is_active, is_root, is_back, menu_type, event_id) 
VALUES ('CONFIRM_TRANSACTION', 'Confirm Transaction', (select max(id) from ussd_menu where menu_code='ENTER_AMOUNT' and is_active =true), 1, true, false, false, 'DYNAMIC', 'CONFIRMATION');

-- Final Success/Failure screens
INSERT INTO ussd_menu (menu_code, title, parent_menu_id, display_order, is_active, is_root, is_back, menu_type, event_id) 
VALUES ('SUCCESS', 'Transaction Successful!\nThank you for using Wilfred Interactive System', (select max(id) from ussd_menu where menu_code='CONFIRM_TRANSACTION' and is_active =true), 1, true, false, false, 'INPUT', 'END');

INSERT INTO ussd_menu (menu_code, title, parent_menu_id, display_order, is_active, is_root, is_back, menu_type, event_id) 
VALUES ('CANCELLED', 'Transaction Cancelled', (select max(id) from ussd_menu where menu_code='CONFIRM_TRANSACTION' and is_active =true), 2, true, false, false, 'INPUT', 'END');