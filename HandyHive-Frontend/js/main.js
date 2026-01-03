/**
 * main.js - Central Logic for Translations & Global Actions
 * Covers: Home, Services, Jobs, Profile, Contact, Download, Subscription
 */

const translations = {
    en: {
        // --- Navigation ---
        nav_home: "Home", nav_services: "Services", nav_jobs: "My Jobs",
        nav_profile: "Profile", nav_contact: "Contact", nav_logout: "Log Out",
        link_back_home: "Back Home",

        // --- Home Page (index.html) ---
        hero_title: "Find Trusted Service Providers",
        hero_sub: "Connect with skilled professionals for all your home needs.",
        btn_explore: "Explore Services",
        btn_app: "Get the App",
        login_sub: "SERVICE PROVIDING APP",

        // --- Contact Page ---
        contact_title: "Contact Us",
        role_dev: "Project Owner & Developer",

        // --- Download App Page ---
        dl_title: "Download HandyHive",
        dl_sub: "Experience the full power of home services on your mobile device.",
        dl_android_step1: "Open <b>Google Play Store</b>.",
        dl_android_step2: "Search for \"HandyHive CZ\".",
        dl_android_step3: "Tap <b>Install</b>.",
        dl_ios_step1: "Open the <b>App Store</b>.",
        dl_ios_step2: "Search for \"HandyHive\".",
        dl_ios_step3: "Tap <b>Get</b>.",

        // --- Services Page ---
        svc_title: "Select Services",
        svc_sub: "Click multiple cards to add them to your cart.",
        cart_title: "Your Cart",
        cart_empty: "No services selected.",
        lbl_urgent: "Urgent? ⚡",
        lbl_total: "Total:",
        btn_checkout: "Proceed to Checkout",

        // --- Service Names ---
        svc_plumbing: "Plumbing", svc_electrician: "Electrician",
        svc_handyman: "Handyman", svc_cleaning: "Deep Cleaning",

        // --- My Jobs Page ---
        title_jobs: "My Jobs", loading: "Loading...",
        header_requests: "Job Requests", header_orders: "My Orders",
        no_jobs: "No jobs found.",
        status_PENDING: "PENDING", status_COMPLETED: "COMPLETED", status_CANCELLED: "CANCELLED", status_IN_PROGRESS: "IN PROGRESS",
        label_customer: "CUSTOMER", label_provider: "PROVIDER",
        modal_title: "Job Details", modal_urgent: "⚡ URGENT REQUEST", modal_desc: "DESCRIPTION", modal_date: "DATE CREATED",
        btn_decline: "Decline", btn_resolve: "Mark Resolved", btn_close: "Close",

        // --- Profile Page ---
        prof_title: "My Profile",
        header_details: "Personal Details",
        lbl_fname: "First Name", lbl_lname: "Last Name",
        lbl_email: "Email", lbl_phone: "Phone Number",
        lbl_role: "Account Type", lbl_bio: "Bio",
        header_settings: "Account Settings",
        btn_edit: "Edit Profile", btn_save: "Save Changes",
        btn_pass: "Change Password", btn_delete: "Delete Account",

        // --- Profile Provider Specifics ---
        header_sub: "Subscription",
        lbl_myservices: "My Services",
        vetted_yes: "Vetted", vetted_no: "Not Vetted",
        plan_standard: "STANDARD PLAN (FREE)",
        plan_monthly: "MONTHLY PLAN",
        plan_annual: "ANNUAL PLAN",
        renewal_next: "Next Renewal:",
        renewal_never: "Next Renewal: Never",
        status_active: "Active",
        btn_upgrade: "Upgrade",
        btn_cancel_sub: "Cancel",

        // --- Roles ---
        role_provider: "Service Provider", role_customer: "Customer",

        // --- Subscription Page / Modal ---
        header_manage: "Manage Plan",
        sub_title: "Find the Perfect Plan",
        sub_desc: "Upgrade to get verified and attract more customers.",

        // Monthly Card
        plan_month_title: "Monthly",
        price_mo: "/mo",
        feat_priority: "Priority Matching",
        feat_nofees: "No Platform Fees",
        feat_cancel: "Cancel Anytime",
        btn_sub_monthly: "Subscribe Monthly",

        // Annual Card
        badge_rec: "RECOMMENDED",
        plan_year_title: "Annual",
        price_yr: "/yr",
        feat_vetted: "Vetted Badge",
        feat_featured: "Featured Profile",
        feat_2months: "2 Months Free",
        btn_go_premium: "Go Premium",

        link_nothanks: "No thanks, return to profile"
    },
    cz: {
        // --- Navigation ---
        nav_home: "Domů", nav_services: "Služby", nav_jobs: "Moje zakázky",
        nav_profile: "Profil", nav_contact: "Kontakt", nav_logout: "Odhlásit",
        link_back_home: "Zpět domů",

        // --- Home Page ---
        hero_title: "Najděte ověřené poskytovatele",
        hero_sub: "Spojte se s kvalifikovanými profesionály pro váš domov.",
        btn_explore: "Prozkoumat služby",
        btn_app: "Stáhnout aplikaci",
        login_sub: "APLIKACE PRO SLUŽBY",

        // --- Contact Page ---
        contact_title: "Kontaktujte nás",
        role_dev: "Vlastník projektu a vývojář",

        // --- Download App Page ---
        dl_title: "Stáhnout HandyHive",
        dl_sub: "Zažijte plnou sílu domácích služeb na svém mobilu.",
        dl_android_step1: "Otevřete <b>Google Play Store</b>.",
        dl_android_step2: "Hledejte \"HandyHive CZ\".",
        dl_android_step3: "Klepněte na <b>Instalovat</b>.",
        dl_ios_step1: "Otevřete <b>App Store</b>.",
        dl_ios_step2: "Hledejte \"HandyHive\".",
        dl_ios_step3: "Klepněte na <b>Získat</b>.",

        // --- Services Page ---
        svc_title: "Vybrat služby",
        svc_sub: "Klikněte na karty pro přidání do košíku.",
        cart_title: "Váš košík",
        cart_empty: "Žádné služby.",
        lbl_urgent: "Spěchá? ⚡",
        lbl_total: "Celkem:",
        btn_checkout: "Přejít k pokladně",

        // --- Service Names ---
        svc_plumbing: "Instalatér", svc_electrician: "Elektrikář",
        svc_handyman: "Hodinový manžel", svc_cleaning: "Úklid",

        // --- My Jobs Page ---
        title_jobs: "Moje zakázky", loading: "Načítání...",
        header_requests: "Žádosti o práci", header_orders: "Moje objednávky",
        no_jobs: "Žádné zakázky nenalezeny.",
        status_PENDING: "ČEKAJÍCÍ", status_COMPLETED: "DOKONČENO", status_CANCELLED: "ZRUŠENO", status_IN_PROGRESS: "V PROCESU",
        label_customer: "ZÁKAZNÍK", label_provider: "POSKYTOVATEL",
        modal_title: "Detaily zakázky", modal_urgent: "⚡ URGENTNÍ", modal_desc: "POPIS", modal_date: "DATUM VYTVOŘENÍ",
        btn_decline: "Odmítnout", btn_resolve: "Hotovo", btn_close: "Zavřít",

        // --- Profile Page ---
        prof_title: "Můj Profil",
        header_details: "Osobní údaje",
        lbl_fname: "Jméno", lbl_lname: "Příjmení",
        lbl_email: "Email", lbl_phone: "Telefon",
        lbl_role: "Typ účtu", lbl_bio: "O mně",
        header_settings: "Nastavení účtu",
        btn_edit: "Upravit profil", btn_save: "Uložit změny",
        btn_pass: "Změnit heslo", btn_delete: "Smazat účet",

        // --- Profile Provider Specifics ---
        header_sub: "Předplatné",
        lbl_myservices: "Moje Služby",
        vetted_yes: "Ověřeno", vetted_no: "Neověřeno",
        plan_standard: "STANDARDNÍ (ZDARMA)",
        plan_monthly: "MĚSÍČNÍ TARIF",
        plan_annual: "ROČNÍ TARIF",
        renewal_next: "Další obnova:",
        renewal_never: "Obnova: Nikdy",
        status_active: "Aktivní",
        btn_upgrade: "Vylepšit",
        btn_cancel_sub: "Zrušit",

        // --- Roles ---
        role_provider: "Poskytovatel", role_customer: "Zákazník",

        // --- Subscription Page ---
        header_manage: "Spravovat tarif",
        sub_title: "Najděte ideální tarif",
        sub_desc: "Vylepšete účet, získejte ověření a více zákazníků.",

        // Monthly Card
        plan_month_title: "Měsíční",
        price_mo: "/měs",
        feat_priority: "Prioritní párování",
        feat_nofees: "Žádné poplatky",
        feat_cancel: "Zrušit kdykoliv",
        btn_sub_monthly: "Předplatit měsíčně",

        // Annual Card
        badge_rec: "DOPORUČENO",
        plan_year_title: "Roční",
        price_yr: "/rok",
        feat_vetted: "Ověřený odznak",
        feat_featured: "Prioritní profil",
        feat_2months: "2 měsíce zdarma",
        btn_go_premium: "Přejít na Premium",

        link_nothanks: "Ne, děkuji, zpět na profil"
    },
    ar: {
        // --- Navigation ---
        nav_home: "الرئيسية", nav_services: "الخدمات", nav_jobs: "وظائفي",
        nav_profile: "الملف الشخصي", nav_contact: "تواصل معنا", nav_logout: "خروج",
        link_back_home: "عودة للرئيسية",

        // --- Home Page ---
        hero_title: "ابحث عن مقدمي خدمات موثوقين",
        hero_sub: "تواصل مع محترفين مهرة لجميع احتياجات منزلك.",
        btn_explore: "تصفح الخدمات",
        btn_app: "حمل التطبيق",
        login_sub: "تطبيق الخدمات المنزلية",

        // --- Contact Page ---
        contact_title: "اتصل بنا",
        role_dev: "مدير الجروب",

        // --- Download App Page ---
        dl_title: "حمل تطبيق HandyHive",
        dl_sub: "جرب القوة الكاملة للخدمات المنزلية على هاتفك.",
        dl_android_step1: "افتح <b>Google Play Store</b>.",
        dl_android_step2: "ابحث عن \"HandyHive CZ\".",
        dl_android_step3: "اضغط <b>تثبيت</b>.",
        dl_ios_step1: "افتح <b>App Store</b>.",
        dl_ios_step2: "ابحث عن \"HandyHive\".",
        dl_ios_step3: "اضغط <b>تحميل</b>.",

        // --- Services Page ---
        svc_title: "اختر الخدمات",
        svc_sub: "انقر فوق البطاقات لإضافتها إلى سلتك.",
        cart_title: "سلة الطلبات",
        cart_empty: "لم يتم تحديد خدمات.",
        lbl_urgent: "طلب عاجل؟ ⚡",
        lbl_total: "الإجمالي:",
        btn_checkout: "إتمام الدفع",

        // --- Service Names ---
        svc_plumbing: "سباكة", svc_electrician: "كهرباء",
        svc_handyman: "صيانة عامة", svc_cleaning: "تنظيف",

        // --- My Jobs Page ---
        title_jobs: "وظائفي", loading: "جار التحميل...",
        header_requests: "طلبات العمل", header_orders: "طلباتي",
        no_jobs: "لا توجد وظائف.",
        status_PENDING: "قيد الانتظار", status_COMPLETED: "مكتمل", status_CANCELLED: "ملغى", status_IN_PROGRESS: "قيد التنفيذ",
        label_customer: "العميل", label_provider: "المزود",
        modal_title: "تفاصيل الوظيفة", modal_urgent: "⚡ طلب عاجل", modal_desc: "الوصف", modal_date: "تاريخ الإنشاء",
        btn_decline: "رفض", btn_resolve: "تم الإنجاز", btn_close: "إغلاق",

        // --- Profile Page ---
        prof_title: "الملف الشخصي",
        header_details: "البيانات الشخصية",
        lbl_fname: "الاسم الأول", lbl_lname: "اسم العائلة",
        lbl_email: "البريد الإلكتروني", lbl_phone: "رقم الهاتف",
        lbl_role: "نوع الحساب", lbl_bio: "نبذة عني",
        header_settings: "إعدادات الحساب",
        btn_edit: "تعديل الملف", btn_save: "حفظ التغييرات",
        btn_pass: "تغيير كلمة المرور", btn_delete: "حذف الحساب",

        // --- Profile Provider Specifics ---
        header_sub: "الاشتراك",
        lbl_myservices: "خدماتي",
        vetted_yes: "موثق", vetted_no: "غير موثق",
        plan_standard: "الخطة المجانية",
        plan_monthly: "الخطة الشهرية",
        plan_annual: "الخطة السنوية",
        renewal_next: "التجديد القادم:",
        renewal_never: "التجديد: لا يوجد",
        status_active: "نشط",
        btn_upgrade: "ترقية",
        btn_cancel_sub: "إلغاء",

        // --- Roles ---
        role_provider: "مزود خدمة", role_customer: "عميل",

        // --- Subscription Page ---
        header_manage: "إدارة الخطة",
        sub_title: "اختر الخطة المثالية",
        sub_desc: "قم بالترقية للحصول على التوثيق وجذب المزيد من العملاء.",

        // Monthly Card
        plan_month_title: "شهري",
        price_mo: "/شهر",
        feat_priority: "مطابقة ذات أولوية",
        feat_nofees: "بدون رسوم منصة",
        feat_cancel: "إلغاء في أي وقت",
        btn_sub_monthly: "اشتراك شهري",

        // Annual Card
        badge_rec: "موصى به",
        plan_year_title: "سنوي",
        price_yr: "/سنة",
        feat_vetted: "شارة التوثيق",
        feat_featured: "ملف مميز",
        feat_2months: "شهران مجاناً",
        btn_go_premium: "انتقل للمميزة",

        link_nothanks: "لا شكراً، العودة للملف الشخصي"
    }
};

/**
 * Changes language and broadcasts event
 */
function changeLanguage(lang) {
    localStorage.setItem('appLang', lang);

    // Update Dropdowns
    const select = document.getElementById('langSelect');
    if (select) select.value = lang;

    // Handle RTL
    if (lang === 'ar') {
        document.documentElement.dir = "rtl";
        document.documentElement.lang = "ar";
    } else {
        document.documentElement.dir = "ltr";
        document.documentElement.lang = lang;
    }

    // Update Text
    document.querySelectorAll('[data-i18n]').forEach(el => {
        const key = el.getAttribute('data-i18n');
        if (translations[lang] && translations[lang][key]) {
            el.innerHTML = translations[lang][key]; // Use innerHTML to allow <b> tags
        }
    });

    // Notify other scripts
    document.dispatchEvent(new Event('langChanged'));
}

function doLogout() {
    if(confirm("Are you sure you want to log out?")) {
        localStorage.removeItem('user');
        window.location.href = 'login.html';
    }
}

// Initialize on Page Load
document.addEventListener('DOMContentLoaded', () => {
    const savedLang = localStorage.getItem('appLang') || 'en';
    changeLanguage(savedLang);
});