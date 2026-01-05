package utils;

public class ContainFeaturesCheck {
    // NHÓM 1: URGENCY & PRESSURE
    public static final String[] URGENCY_WORDS = {
            "act", "act fast", "act immediately", "act now", "act now!", "action",
            "action required", "apply here", "apply now", "apply online", "before it's too late",
            "call", "call free", "call now", "call now!", "can't miss", "click", "click below",
            "click here", "click me", "click now", "click to get", "click to verify",
            "contact us immediately", "deal ending soon", "do it now", "do it today",
            "don't delete", "don't hesitate", "don't waste time", "expire", "expires today",
            "final call", "final notice", "for instant access", "get it away", "get it now",
            "get now", "get started", "get started now", "hurry up", "immediate action required",
            "immediately", "important information", "important update", "instant", "instant access",
            "last warning", "limited time", "now", "now only", "offer expires", "once in a lifetime",
            "only", "only a few left", "order now", "order today", "please read", "purchase now",
            "supplies are limited", "take action", "take action now", "this won’t last",
            "time limited", "today", "top urgent", "urgent", "warning message",
            "what are you waiting for?", "while supplies last"
    };

    // NHÓM 2: MONEY & FINANCE
    public static final String[] MONEY_WORDS = {
            "$$$", "€€€", "£££", "additional income", "avoid bankruptcy", "bad credit",
            "bank", "bankruptcy", "big bucks", "billion", "billion dollars", "billionaire",
            "cash", "cash bonus", "cash out", "cash out now", "cash-out", "cashcashcash",
            "casino", "casino bonus", "cents on the dollar", "check", "check or money order",
            "consolidate debt", "cost", "costs", "credit", "credit bureaus", "credit card",
            "credit card offers", "credit or debit", "debt", "dollar", "dollars",
            "double your cash", "double your income", "double your money", "double your wealth",
            "earn", "earn $", "earn cash", "earn extra cash", "earn extra income",
            "earn from home", "earn money", "earn monthly", "earn per month", "earn per week",
            "earn per year", "easy income", "easy terms", "expect to earn", "extra cash",
            "extra income", "fast cash", "financial freedom", "full refund", "gamble online",
            "get paid", "income", "increase revenue", "increase sales", "increase traffic",
            "insurance", "investment", "investment advice", "jackpot", "loans", "make $",
            "make money", "million dollars", "money", "money-back guarantee", "mortgage",
            "mortgage rates", "one hundred percent free", "online betting", "online casino",
            "online gaming", "payment details needed", "poker tournament", "potential earnings",
            "price", "price protection", "profits", "pure profit", "quote", "refinance",
            "refund", "save $", "save big money", "save up to", "slots jackpot",
            "subject to credit", "us dollars", "wealth", "winning numbers", "your income"
    };

    // NHÓM 3: SCAM, FRAUD & TOO GOOD TO BE TRUE
    public static final String[] SCAM_FRAUD_WORDS = {
            "100% free", "100% guaranteed", "100% satisfied", "access your account",
            "account update", "activate now", "amazed", "amazing", "amazing stuff",
            "antivirus", "be amazed", "be surprised", "be your own boss", "being a member",
            "bet now", "big win", "bonus", "cancel now", "cancellation required",
            "certified", "change password", "claim now", "claim your discount",
            "click to remove", "click to win", "confidential information", "confirm your details",
            "congratulations", "cyber monday", "data breach", "download now", "exclusive access",
            "fantastic deal", "fantastic offer", "free access", "free antivirus", "free chips",
            "free consultation", "free gift", "free hosting", "free info", "free investment",
            "free membership", "free money", "free preview", "free priority mail", "free quote",
            "free spins", "free trial", "get it away", "giveaway", "guaranteed deposit",
            "guaranteed results", "hello friend", "hidden", "home based", "hosting",
            "info you requested", "information you requested", "install now", "join millions",
            "log in now", "lucky chance", "miracle", "miracle cure", "multi-level marketing",
            "new login detected", "no catch", "no cost", "no credit check", "no hidden costs",
            "no obligation", "no strings attached", "not spam", "password reset",
            "phishing alert", "prize", "promise", "risk-free", "risk-free bet",
            "satisfaction guaranteed", "score with babes", "secret formula", "secure payment",
            "security breach", "security update", "sign up free", "spin to win",
            "stop snoring", "suspicious activity", "the best", "this isn't a scam",
            "this won't last", "thousands", "unbelievable", "unlimited", "update account",
            "verify identity", "vip offer", "winner", "winner announced", "won",
            "wonderful", "xxx", "you are a winner", "you have been selected",
            "you will not believe your eyes"
    };

    // NHÓM 4: MARKETING & SALES
    public static final String[] MARKETING_WORDS = {
            "100% off", "50% off", "access", "access now", "affordable", "affordable deal",
            "all new", "amazing deal", "amazing offer", "bargain", "best bargain",
            "best deal", "best offer", "best price", "best rates", "buy", "buy direct",
            "buy now", "buy today", "card accepted", "cards accepted", "cheap",
            "clearance", "coupon", "deal", "discount", "drastically reduced",
            "exclusive deal", "fantastic", "for free", "for just $", "for only",
            "for you", "gift", "great news", "great offer", "hot deal", "incredible deal",
            "lowest price", "luxury", "marketing", "mass email", "membership",
            "month trial offer", "more internet traffic", "new customers only",
            "offer", "one time", "online marketing", "online pharmacy", "order",
            "promotion", "purchase", "sale", "sales", "search engine", "search engines",
            "special promotion", "subscribe", "trial", "unbeatable offer", "unsubscribe",
            "valuable", "web traffic", "why pay more?"
    };

    // NHÓM 5: HEALTH & GIMMICKS
    public static final String[] HEALTH_GIMMICK_WORDS = {
            "100% natural", "all natural", "anti-aging", "certified organic", "clinical trial",
            "cure", "cure for", "diagnostics", "diet", "diet pill", "doctor recommended",
            "double blind study", "eliminate", "fat burner", "fast weight loss", "get slim",
            "guaranteed weight loss", "hair growth", "herbal", "lose weight", "lose weight fast",
            "medical breakthrough", "medicine", "natural remedy", "no prescription needed",
            "over-the-counter", "pain relief", "pharmacy", "prescription drugs",
            "reverses aging", "reverse aging", "safe and effective", "scientifically proven",
            "viagra", "vicodin", "weight loss", "xanax", "youthful skin"
    };
    // NHÓM 6: SECURITY PHISHING & FAKE ALERTS (Giả mạo cảnh báo bảo mật)
    public static final String[] SECURITY_WORDS = {
            "account", "verify", "security", "suspended", "locked",
            "unauthorized", "login", "password", "alert", "notification"
    };
    public static final String[] STRANGE_LINKS = {
            "http://", "https://", "www",
            "bit.ly/", "tinyurl.com", "goo.gl/", "gg.gg",
            "t.co", "cutt.ly", "is.gd", "ouo.io",
            "click.php", "redirect", "track.php",
            ".xyz/", ".tk/", ".ml/", ".ga/", ".cf/"  // TLD miễn phí thường bị lạm dụng
    };
    public static final String[] SPECIAL_CHARS = {
            "!", "@", "#", "$", "%", "&", "*",
            "?", "...", "-",
            "★", "☆", "£", "¢", "€", "¥",
            "✓", "✔", "✖", "→", "⇒"
    };

    public static int containsUrgencyWords(String text) {
        return containsWord(text, URGENCY_WORDS);
    }

    public static int containsMoneyWords(String text) {
        return containsWord(text, MONEY_WORDS);
    }

    public static int containsScamFraudWords(String text) {
        return containsWord(text, SCAM_FRAUD_WORDS);
    }

    public static int containsMarketingWords(String text) {
        return containsWord(text, MARKETING_WORDS);
    }

    public static int containsHealthWords(String text) {
        return containsWord(text, HEALTH_GIMMICK_WORDS);
    }

    public static int containSecurityWords(String text) {
        return containsWord(text, SECURITY_WORDS);
    }

    public static int containsStrangeLink(String wordsInput) {
        return containsWord(wordsInput, STRANGE_LINKS);
    }

    public static int containsSpecialChar(String wordsInput) {
        return containSpecialChar(wordsInput, SPECIAL_CHARS);
    }

    // Hàm kiểm tra đầu vào theo điều kiện patterns
    public static int containsWord(String wordsInput, String[] patterns) {
        if (wordsInput == null || wordsInput.isBlank() || patterns == null || patterns.length == 0) return 0;
        String wordsLowerCase = wordsInput.toLowerCase();
        int count = 0;
        for (String pattern : patterns) {
            if (pattern == null || pattern.isBlank()) continue;
            String p = pattern.toLowerCase();
            if (wordsLowerCase.contains(p)) {
                count++;
            }
        }
        if (count == 0) return 0;
        if (count <= 2) return 1;
        return 2;
    }

    // Hàm kiểm tra điều kiện chữ in hoa
    public static int containsUpperCase(String wordsInput) {
        if (wordsInput == null || wordsInput.isBlank()) return 0;
        int upperCount = 0;
        int letterCount = 0; // Chỉ đếm chữ cái

        for (int i = 0; i < wordsInput.length(); i++) {
            char c = wordsInput.charAt(i);
            if (Character.isLetter(c)) {
                // đếm toàn bộ chữ
                letterCount++;
                if (Character.isUpperCase(c)) {
                    // đếm mỗi chữ in hoa
                    upperCount++;
                }
            }
        }

        if (letterCount == 0) return 0;

        // tính tỷ lệ
        double ratio = (double) upperCount / letterCount;

        if (ratio < 0.15) return 0;      // < 15% chữ hoa thì 0
        if (ratio <= 0.35) return 1;     // 15-35% chữ hoa thì 1
        return 2;                         // > 35% chữ hoa thì 2
    }

    // Hàm kiểm tra nội dung email dài bao nhiêu
    public static int howLongDescription(String wordsInput) {
        if (wordsInput == null || wordsInput.isEmpty()) return 0;
        int textLength = wordsInput.split("\\s+").length;
        if (textLength < 100) return 0;
        if (textLength <= 300) return 1;
        return 2;
    }

    // Hàm kiểm tra điều kiện chứa kí tự đặc biệt
    public static int containSpecialChar(String wordsInput, String[] patterns) {
        if (wordsInput == null || wordsInput.isEmpty()) return 0;

        int specialCharCount = 0;
        for (int i = 0; i < wordsInput.length(); i++) {
            String currentChar = String.valueOf(wordsInput.charAt(i));
            for (String pattern : patterns) {
                if (currentChar.equals(pattern)) {
                    // đếm mỗi kí tự đặc biệt
                    specialCharCount++;
                    break; // Tránh đếm trùng
                }
            }
        }

        double ratio = (double) specialCharCount / wordsInput.length();

        if (ratio < 0.02) return 0;      // < 2% thì 0
        if (ratio <= 0.05) return 1;     // 2-5% thì 1
        return 2;                         // > 5% thì 2
    }
}
