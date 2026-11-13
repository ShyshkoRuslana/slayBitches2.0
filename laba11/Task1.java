import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

// Головний клас файлу (обов'язково має збігатися з назвою файлу Task1.java)
public class Task1 {

    public static void runDemo() {
        System.out.println("=== ДЕМОНСТРАЦІЯ ЗАВДАННЯ 1: ПІДКЛЮЧЕННЯ ПЛАТІЖНОГО ТЕРМІНАЛУ (ADAPTER) ===\n");

        // Створюємо сторонній термінал та загортаємо його в адаптер
        BankTerminalApi bankTerminal = new BankTerminalApi("POS-01");
        IPaymentProcessor adapter = new BankTerminalAdapter(bankTerminal);

        // Каса працює виключно з інтерфейсом IPaymentProcessor
        CinemaCashDesk cashDesk = new CinemaCashDesk("Каса №1", adapter);

        // Сценарій 1: Успішна оплата квитка на 180 грн
        System.out.println("--- Крок 1: Успішна оплата квитка (180 грн) ---");
        cashDesk.sellTicket("Дюна: Частина друга", 180.00);

        System.out.println();

        // Сценарій 2: Емуляція відмови терміналу
        System.out.println("--- Крок 2: Емуляція відмови терміналу ---");
        bankTerminal.setSimulateDecline(true);
        cashDesk.sellTicket("Оппенгеймер", 210.00);
    }

    public static void main(String[] args) {
        runDemo();
    }
}

// 1. Утиліта для виводу журналу подій у форматі [час] [джерело] подія
class EventLogger {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void log(String source, String message) {
        String time = LocalTime.now().format(FORMATTER);
        System.out.printf("[%s] [%s] %s%n", time, source, message);
    }
}

// 2. Модель результату оплати у форматі каси
record PaymentResult(boolean isSuccess, String message) {
    @Override
    public String toString() {
        return isSuccess ? "УСПІХ (" + message + ")" : "ВІДМОВА (" + message + ")";
    }
}

// 3. Цільовий інтерфейс каси (Target)
interface IPaymentProcessor {
    PaymentResult processPayment(double amountInUah);
}

// 4. Адаптований клас стороннього банківського терміналу (Adaptee)
class BankTerminalApi {
    private final String terminalId;
    private boolean simulateDecline = false;

    public BankTerminalApi(String terminalId) {
        this.terminalId = terminalId;
    }

    public void setSimulateDecline(boolean simulateDecline) {
        this.simulateDecline = simulateDecline;
    }

    // Несумісний метод: приймає копійки і повертає числовий код
    public int makeTransaction(int amountInKopecks) {
        EventLogger.log("BankTerminalApi:" + terminalId, "Отримано запит на списання: " + amountInKopecks + " коп.");

        if (simulateDecline) {
            int errorCode = 101;
            EventLogger.log("BankTerminalApi:" + terminalId, "Транзакцію відхилено. Код відповіді терміналу: " + errorCode);
            return errorCode;
        }

        int successCode = 0;
        EventLogger.log("BankTerminalApi:" + terminalId, "Транзакцію виконано. Код відповіді терміналу: " + successCode);
        return successCode;
    }
}

// 5. Адаптер (Adapter) — перекладач між касою та терміналом
class BankTerminalAdapter implements IPaymentProcessor {
    private final BankTerminalApi terminal;

    public BankTerminalAdapter(BankTerminalApi terminal) {
        this.terminal = terminal;
    }

    @Override
    public PaymentResult processPayment(double amountInUah) {
        EventLogger.log("BankTerminalAdapter", "Перетворення суми " + amountInUah + " грн у копійки...");

        // Конвертуємо гривні в копійки
        int kopecks = (int) Math.round(amountInUah * 100);

        // Викликаємо метод терміналу
        int responseCode = terminal.makeTransaction(kopecks);

        // Перекладаємо код відповіді у зрозумілий для каси формат
        PaymentResult result = mapResponseCode(responseCode);
        EventLogger.log("BankTerminalAdapter", "Код " + responseCode + " перетворено у відповідь каси: " + result);

        return result;
    }

    private PaymentResult mapResponseCode(int code) {
        return switch (code) {
            case 0 -> new PaymentResult(true, "Оплата пройшла успішно");
            case 101 -> new PaymentResult(false, "Недостатньо коштів на картці");
            case 504 -> new PaymentResult(false, "Помилка зв'язку з банком-еквайром");
            default -> new PaymentResult(false, "Невідома помилка терміналу (код " + code + ")");
        };
    }
}

// 6. Клієнт — Каса кінотеатру (Client)
class CinemaCashDesk {
    private final String deskName;
    private IPaymentProcessor paymentProcessor;

    public CinemaCashDesk(String deskName, IPaymentProcessor paymentProcessor) {
        this.deskName = deskName;
        this.paymentProcessor = paymentProcessor;
    }

    public void setPaymentProcessor(IPaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
        EventLogger.log(deskName, "Підключено новий платіжний процесор.");
    }

    public void sellTicket(String movieTitle, double priceUah) {
        EventLogger.log(deskName, "Оформлення квитка на фільм «" + movieTitle + "». До сплати: " + priceUah + " грн.");

        PaymentResult result = paymentProcessor.processPayment(priceUah);

        if (result.isSuccess()) {
            EventLogger.log(deskName, "Друк квитка... Статус: " + result);
        } else {
            EventLogger.log(deskName, "Продаж скасовано! Причина: " + result.message());
        }
    }
}