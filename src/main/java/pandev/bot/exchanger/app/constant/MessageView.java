package pandev.bot.exchanger.app.constant;

public class MessageView {
    public static final String SHOW_START = """
            Enter usd - to get rate
            Enter kzt - to get rate
            Enter convert - to choose one currency
            Enter help - to get help info
            Happy using my bot: my dear 
            """;
    public static final String SHOW_HELP = """
            Enter usd - to get rate
            Enter kzt - to get rate
            Enter convert - to choose one currency
            """;
    public static final String SHOW_UNDEFINED = "Undefined command, try again!";
    public static final String WAIT_FOR_INPUT = "Enter value: ";
    public static final String ERROR_COMMAND = "Command wasn't confirmed. Try later!";
    public static final String ERROR_API = "Connection to api is failed";
    public static final String CHOOSE_CURRENT = "Choose current: ";
    public static final String INFO_START = "get welcome message";
    public static final String INFO_HELP = "get all commands";
    public static final String INFO_USD = "get usd currency";
    public static final String INFO_KZT = "get kzt currency";
    public static final String INFO_CONVERT = "convert your currency";
    public static final String INFO_USD_RATE = "USD in %s is %s RUB";
    public static final String INFO_KZT_RATE = "KZT in %s is %S RUB";
}

