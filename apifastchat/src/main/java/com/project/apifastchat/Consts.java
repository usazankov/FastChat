package com.project.apifastchat;

public class Consts {
    // Объект список пользователей
    public static final String USERS_LIST = "Users_list"; //Объект список пользователей
    public static final String COUNT_USERS_ONLINE = "Count_users_online"; //Количество пользователей в онлайне
    // Описание объекта пользователя:
    public static final String USER_OBJ = "User_obj";  //Объект пользователь
    public static final String USER_ID = "User_id"; //Идентификатор пользователя
    public static final String USER_NAME = "User_name"; //Имя пользователя
    public static final String USER_STATUS = "User_status"; //Объект статус пользователя
    public static final String USER_ONLINE = "u_online"; //Пользователь в онлайне
    public static final String USER_OFFLINE = "u_offline"; //Пользователь в офлайне
    public static final String USER_INFO = "User_info"; //Какая-то информация о пользователе
    // Описание объекта сообщение
    public static final String MESSAGE_OBJ = "Message_obj"; //Объект сообщение
    public static final String MESSAGE_DATE = "Message_data"; //Дата сообщения
    public static final String MESSAGE_TIME = "Message_time"; //Время сообщения
    public static final String MESSAGE_BODY = "Message_body"; //Тело сообщения
    //Содержится User_id - от кого
    //Здесь должен быть объект Users_list, если его нет, то сообщение отправляется всем

    // Описание объекта команда:
    public static final String COMMAND_OBJ = "Command_obj"; //Объект команда
    public static final String COMMAND_ID = "Command_id"; //Идентификатор команды
    public static final String COMMAND_ARR_PARAM = "Command_param"; //Объект параметры команды
    public static final String COMMAND_PARAM1 = "Command_param_1"; //1 параметр команды
    public static final String COMMAND_PARAM2 = "Command_param_2"; //2 параметр команды
    // итд...

    // Описание идентификаторов команд (список возможных полей для поля Command_id)
    public static final String C_AUTH_REQ = "c_auth_req"; //Запрос на авторизацию
    public static final String C_GET_COUNT_ONLINE = "c_get_count_online"; //Получить количество пользователей онлайн
    public static final String C_GET_ALL_USERS = "c_get_users"; //Получить список всех пользователей
    public static final String C_GET_ONLINE_USERS = "c_get_online_users"; //Получить список пользователей в онлайне
    public static final String C_GET_USER_INFO = "c_get_user_info"; //Получить подробную информацию о пользователе (ID пользователя передается в Command_param1)
    public static final String C_SEND_MESSAGE = "c_send_message"; //Отправить сообщение в чат
    public static final String C_GET_EVENTS = "c_get_events_users"; //Получить события от сервера
    //(Сервер должен вернуть список пользователей, изменивших свое состояние, имя или еще что-то)

    public static final String EVENT_OBJ = "Event_obj"; //Объект событие от сервера
    public static final String EVENT_ID = "Event_id"; //Идентификатор события
    //В этом объекте (EVENT_OBJ) может содержаться объект USERS_LIST - список пользователей, изменивших свое состояние, имя или еще что-то
    public static final String MESSAGES_LIST = "Messages_list"; //Список сообщений от пользователей

    // Описание идентификаторов команд (список возможных полей для поля Event_id)
    public static final String E_UNDEFINED = "e_undefined"; //Неизвестный Event
    public static final String E_CONNECT_USER = "e_connect_user"; //В этом типе события находится объект USER_OBJ
    public static final String E_DISCONNECT_USER = "e_disconnect_user"; //В этом типе события находится объект USER_OBJ
    public static final String E_MESSAGE = "e_message"; //Сообщение
    public static final String E_USERS_LIST = "e_users_list"; //Список пользователей

    public static final String CODE_RESP = "Code_resp"; //Код ответа сервера

    //Коды ответа сервера
    public static final String USER_IS_NOT_REG = "user_is_not_reg"; //Пользователь не зарегистрирован
    public static final String C_SUCCESS = "c_success"; //Успешное выполнение запроса
    public static final String C_ERROR = "c_error"; //Ошибка выполнения запроса


    public static final String NO_MSG_ID = "no_msg_id";// При таком значении в запросе id_msg сервер не будет отвечать id_msg

    //Объект ошибка
    public static final String ERROR_OBJ = "Error_obj"; //Объект ошибка
    public static final String ERROR_ID = "Error_id"; //Идентификатор ошибки
    // Описание идентификаторов ошибок (список возможных полей для поля ERROR_ID)
    public static final String ERROR_UNDEFINED = "Error_undefined"; //Неизвестная ошибка
    public static final String ERROR_PARSE = "Error_parse"; //ошибка при парсинге (отправлен не JSon-объект)
    public static final String ERROR_MISSING_USER_ID = "Error_missing_user_id"; //В запросе нет UserId
    public static final String ERROR_EMPTY_MESSAGE = "Error_empty_message"; //В запросе пустое сообщение
    public static final String ERROR_NAME_INVALID = "Error_name_invalid"; //Недопустимое имя пользователя
    public static final String ERROR_NAME_IS_USE = "Error_name_is_use"; //Имя пользователя уже используется
    //итд

    public static final String DATE = "Date"; //Дата
    public static final String TIME = "Time"; //Время
}
