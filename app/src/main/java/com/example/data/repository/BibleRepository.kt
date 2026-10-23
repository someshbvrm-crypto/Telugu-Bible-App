package com.example.data.repository

import com.example.data.model.BibleBook
import com.example.data.model.BibleVerse
import com.example.data.model.DailyVerse
import com.example.data.model.ReadingDayItem
import com.example.data.model.Testament

object BibleRepository {

    val books: List<BibleBook> = listOf(
        // Old Testament (పాత నిబంధన - 39 గ్రంథములు)
        // 1. ధర్మశాస్త్రము (Law)
        BibleBook("GEN", "ఆదికాండము", "Genesis", "ఆది", Testament.OLD, 50, "ధర్మశాస్త్రము (Law)", "ఆది"),
        BibleBook("EXO", "నిర్గమకాండము", "Exodus", "నిర్గ", Testament.OLD, 40, "ధర్మశాస్త్రము (Law)", "నిర్గ"),
        BibleBook("LEV", "లేవీయకాండము", "Leviticus", "లేవీ", Testament.OLD, 27, "ధర్మశాస్త్రము (Law)", "లేవీ"),
        BibleBook("NUM", "సంఖ్యాకాండము", "Numbers", "సంఖ్యా", Testament.OLD, 36, "ధర్మశాస్త్రము (Law)", "సం"),
        BibleBook("DEU", "ద్వితీయోపదేశకాండము", "Deuteronomy", "ద్వితీ", Testament.OLD, 34, "ధర్మశాస్త్రము (Law)", "ద్వి"),

        // 2. చరిత్ర (History)
        BibleBook("JOS", "యెహోషువ", "Joshua", "యెహో", Testament.OLD, 24, "చరిత్ర (History)", "యె"),
        BibleBook("JDG", "న్యాయాధిపతులు", "Judges", "న్యాయా", Testament.OLD, 21, "చరిత్ర (History)", "న్యా"),
        BibleBook("RUT", "రూతు", "Ruth", "రూతు", Testament.OLD, 4, "చరిత్ర (History)", "రూ"),
        BibleBook("1SA", "1 సమూయేలు", "1 Samuel", "1సమూ", Testament.OLD, 31, "చరిత్ర (History)", "1స"),
        BibleBook("2SA", "2 సమూయేలు", "2 Samuel", "2సమూ", Testament.OLD, 24, "చరిత్ర (History)", "2స"),
        BibleBook("1KI", "1 రాజులు", "1 Kings", "1రాజు", Testament.OLD, 22, "చరిత్ర (History)", "1రా"),
        BibleBook("2KI", "2 రాజులు", "2 Kings", "2రాజు", Testament.OLD, 25, "చరిత్ర (History)", "2రా"),
        BibleBook("1CH", "1 దినవృత్తాంతములు", "1 Chronicles", "1దిన", Testament.OLD, 29, "చరిత్ర (History)", "1ది"),
        BibleBook("2CH", "2 దినవృత్తాంతములు", "2 Chronicles", "2దిన", Testament.OLD, 36, "చరిత్ర (History)", "2ది"),
        BibleBook("EZR", "ఎజ్రా", "Ezra", "ఎజ్రా", Testament.OLD, 10, "చరిత్ర (History)", "ఎజ్"),
        BibleBook("NEH", "నెహెమ్యా", "Nehemiah", "నెహె", Testament.OLD, 13, "చరిత్ర (History)", "నె"),
        BibleBook("EST", "ఎస్తేరు", "Esther", "ఎస్తే", Testament.OLD, 10, "చరిత్ర (History)", "ఎస్"),

        // 3. కావ్యములు & జ్ఞానసాహిత్యము (Poetry & Wisdom)
        BibleBook("JOB", "యోబు", "Job", "యోబు", Testament.OLD, 42, "కావ్యములు (Poetry & Wisdom)", "యో"),
        BibleBook("PSA", "కీర్తనలు", "Psalms", "కీర్తన", Testament.OLD, 150, "కావ్యములు (Poetry & Wisdom)", "కీర్త"),
        BibleBook("PRO", "సామెతలు", "Proverbs", "సామె", Testament.OLD, 31, "కావ్యములు (Poetry & Wisdom)", "సామె"),
        BibleBook("ECC", "ప్రసంగి", "Ecclesiastes", "ప్రసం", Testament.OLD, 12, "కావ్యములు (Poetry & Wisdom)", "ప్రస"),
        BibleBook("SNG", "పరమగీతము", "Song of Solomon", "పరమ", Testament.OLD, 8, "కావ్యములు (Poetry & Wisdom)", "పర"),

        // 4. ప్రవక్తలు (Prophets)
        BibleBook("ISA", "యెషయా", "Isaiah", "యెష", Testament.OLD, 66, "ప్రవక్తలు (Prophets)", "యె"),
        BibleBook("JER", "యిర్మీయా", "Jeremiah", "యిర్మీ", Testament.OLD, 52, "ప్రవక్తలు (Prophets)", "యి"),
        BibleBook("LAM", "విలాపవాక్యములు", "Lamentations", "విలా", Testament.OLD, 5, "ప్రవక్తలు (Prophets)", "వి"),
        BibleBook("EZK", "యెహెజ్కేలు", "Ezekiel", "యెహె", Testament.OLD, 48, "ప్రవక్తలు (Prophets)", "యె"),
        BibleBook("DAN", "దానియేలు", "Daniel", "దాని", Testament.OLD, 12, "ప్రవక్తలు (Prophets)", "దా"),
        BibleBook("HOS", "హోషేయ", "Hosea", "హోషే", Testament.OLD, 14, "ప్రవక్తలు (Prophets)", "హో"),
        BibleBook("JOL", "యోవేలు", "Joel", "యోవే", Testament.OLD, 3, "ప్రవక్తలు (Prophets)", "యో"),
        BibleBook("AMO", "ఆమోసు", "Amos", "ఆమో", Testament.OLD, 9, "ప్రవక్తలు (Prophets)", "ఆ"),
        BibleBook("OBD", "ఓబద్యా", "Obadiah", "ఓబ", Testament.OLD, 1, "ప్రవక్తలు (Prophets)", "ఓ"),
        BibleBook("JON", "యోనా", "Jonah", "యోనా", Testament.OLD, 4, "ప్రవక్తలు (Prophets)", "యో"),
        BibleBook("MIC", "మీకా", "Micah", "మీకా", Testament.OLD, 7, "ప్రవక్తలు (Prophets)", "మీ"),
        BibleBook("NAM", "నహూము", "Nahum", "నహూ", Testament.OLD, 3, "ప్రవక్తలు (Prophets)", "న"),
        BibleBook("HAB", "హబక్కూకు", "Habakkuk", "హబ", Testament.OLD, 3, "ప్రవక్తలు (Prophets)", "హ"),
        BibleBook("ZEP", "జెఫన్యా", "Zephaniah", "జెఫ", Testament.OLD, 3, "ప్రవక్తలు (Prophets)", "జె"),
        BibleBook("HAG", "హగ్గయి", "Haggai", "హగ్గ", Testament.OLD, 2, "ప్రవక్తలు (Prophets)", "హగ్"),
        BibleBook("ZEC", "జెకర్యా", "Zechariah", "జెక", Testament.OLD, 14, "ప్రవక్తలు (Prophets)", "జె"),
        BibleBook("MAL", "మలాకీ", "Malachi", "మలా", Testament.OLD, 4, "ప్రవక్తలు (Prophets)", "మ"),

        // New Testament (క్రొత్త నిబంధన - 27 గ్రంథములు)
        // 1. సువార్తలు (Gospels)
        BibleBook("MAT", "మత్తయి", "Matthew", "మత్త", Testament.NEW, 28, "సువార్తలు (Gospels)", "మత్త"),
        BibleBook("MRK", "మార్కు", "Mark", "మార్కు", Testament.NEW, 16, "సువార్తలు (Gospels)", "మార్కు"),
        BibleBook("LUK", "లూకా", "Luke", "లూకా", Testament.NEW, 24, "సువార్తలు (Gospels)", "లూ"),
        BibleBook("JHN", "యోహాను", "John", "యోహా", Testament.NEW, 21, "సువార్తలు (Gospels)", "యోహా"),

        // 2. సంఘ చరిత్ర (History)
        BibleBook("ACT", "అపొస్తలుల కార్యములు", "Acts", "అపొ", Testament.NEW, 28, "చరిత్ర (History)", "అపొ"),

        // 3. పౌలు పత్రికలు (Pauline Epistles)
        BibleBook("ROM", "రోమీయులకు", "Romans", "రోమా", Testament.NEW, 16, "పౌలు పత్రికలు (Epistles)", "రోమా"),
        BibleBook("1CO", "1 కొరింథీయులకు", "1 Corinthians", "1కొరిం", Testament.NEW, 16, "పౌలు పత్రికలు (Epistles)", "1కొ"),
        BibleBook("2CO", "2 కొరింథీయులకు", "2 Corinthians", "2కొరిం", Testament.NEW, 13, "పౌలు పత్రికలు (Epistles)", "2కొ"),
        BibleBook("GAL", "గలతీయులకు", "Galatians", "గలతీ", Testament.NEW, 6, "పౌలు పత్రికలు (Epistles)", "గల"),
        BibleBook("EPH", "ఎఫెసీయులకు", "Ephesians", "ఎఫెసీ", Testament.NEW, 6, "పౌలు పత్రికలు (Epistles)", "ఎఫె"),
        BibleBook("PHP", "ఫిలిప్పీయులకు", "Philippians", "ఫిలిప్పీ", Testament.NEW, 4, "పౌలు పత్రికలు (Epistles)", "ఫిలి"),
        BibleBook("COL", "కొలొస్సయులకు", "Colossians", "కొలొస్స", Testament.NEW, 4, "పౌలు పత్రికలు (Epistles)", "కొలొ"),
        BibleBook("1TH", "1 థెస్సలొనీకయులకు", "1 Thessalonians", "1థెస్స", Testament.NEW, 5, "పౌలు పత్రికలు (Epistles)", "1థె"),
        BibleBook("2TH", "2 థెస్సలొనీకయులకు", "2 Thessalonians", "2థెస్స", Testament.NEW, 3, "పౌలు పత్రికలు (Epistles)", "2థె"),
        BibleBook("1TI", "1 తిమోతికి", "1 Timothy", "1తిమో", Testament.NEW, 6, "పౌలు పత్రికలు (Epistles)", "1తి"),
        BibleBook("2TI", "2 తిమోతికి", "2 Timothy", "2తిమో", Testament.NEW, 4, "పౌలు పత్రికలు (Epistles)", "2తి"),
        BibleBook("TIT", "తీతుకు", "Titus", "తీతు", Testament.NEW, 3, "పౌలు పత్రికలు (Epistles)", "తీతు"),
        BibleBook("PHM", "ఫిలేమోనుకు", "Philemon", "ఫిలే", Testament.NEW, 1, "పౌలు పత్రికలు (Epistles)", "ఫిలే"),

        // 4. సాధారణ పత్రికలు (General Epistles)
        BibleBook("HEB", "హెబ్రీయులకు", "Hebrews", "హెబ్రీ", Testament.NEW, 13, "సాధారణ పత్రికలు (General)", "హె"),
        BibleBook("JAS", "యాకోబు", "James", "యాకోబు", Testament.NEW, 5, "సాధారణ పత్రికలు (General)", "యా"),
        BibleBook("1PE", "1 పేతురు", "1 Peter", "1పేతు", Testament.NEW, 5, "సాధారణ పత్రికలు (General)", "1పే"),
        BibleBook("2PE", "2 పేతురు", "2 Peter", "2పేతు", Testament.NEW, 3, "సాధారణ పత్రికలు (General)", "2పే"),
        BibleBook("1JN", "1 యోహాను", "1 John", "1యోహా", Testament.NEW, 5, "సాధారణ పత్రికలు (General)", "1యో"),
        BibleBook("2JN", "2 యోహాను", "2 John", "2యోహా", Testament.NEW, 1, "సాధారణ పత్రికలు (General)", "2యో"),
        BibleBook("3JN", "3 యోహాను", "3 John", "3యోహా", Testament.NEW, 1, "సాధారణ పత్రికలు (General)", "3యో"),
        BibleBook("JUD", "యూదా", "Jude", "యూదా", Testament.NEW, 1, "సాధారణ పత్రికలు (General)", "యూ"),

        // 5. ప్రకటన గ్రంథము (Prophecy)
        BibleBook("REV", "ప్రకటన గ్రంథము", "Revelation", "ప్రకటన", Testament.NEW, 22, "ప్రకటన (Prophecy)", "ప్రక")
    )

    // Extensive bundled Telugu Scripture text for instant offline reading
    private val versesDatabase: Map<String, List<BibleVerse>> = mapOf(
        // Genesis 1
        "GEN_1" to listOf(
            BibleVerse("GEN", "ఆదికాండము", "Genesis", 1, 1, "ఆదియందు దేవుడు భూమ్యాకాశములను సృజించెను.", "In the beginning God created the heavens and the earth."),
            BibleVerse("GEN", "ఆదికాండము", "Genesis", 1, 2, "భూమి నిరాకారముగాను శూన్యముగాను ఉండెను; చీకటి అగాధజలములమీద కమ్మియుండెను; దేవుని ఆత్మ జలములపైన అల్లాడుచుండెను.", "The earth was without form and void, and darkness was over the face of the deep."),
            BibleVerse("GEN", "ఆదికాండము", "Genesis", 1, 3, "దేవుడు—వెలుగు కలుగును గాక అని పలుకగా వెలుగు కలిగెను.", "And God said, 'Let there be light,' and there was light."),
            BibleVerse("GEN", "ఆదికాండము", "Genesis", 1, 4, "వెలుగు మంచిదైనట్టు దేవుడు చూచెను; దేవుడు వెలుగును చీకటిని వేరుపరచెను.", "God saw that the light was good, and he separated the light from the darkness."),
            BibleVerse("GEN", "ఆదికాండము", "Genesis", 1, 5, "దేవుడు వెలుగునకు పగలనియు, చీకటికి రాత్రియనియు పేరుపెట్టెను. అస్తమయమును ఉదయమును కలుగగా ఒక దినమాయెను.", "God called the light Day, and the darkness he called Night."),
            BibleVerse("GEN", "ఆదికాండము", "Genesis", 1, 26, "దేవుడు—మన స్వరూపమందు మన పోలికచొప్పున నరులను చేయుదము; వారు సముద్రపు చేపలను ఆకాశపక్షులను భూమిని ఏలుదురు గాక అనెను.", "Then God said, 'Let us make man in our image, after our likeness.'"),
            BibleVerse("GEN", "ఆదికాండము", "Genesis", 1, 27, "దేవుడు తన స్వరూపమందు నరుని సృజించెను; దేవుని స్వరూపమందు వాని సృజించెను; స్త్రీనిగాను పురుషునిగాను వారిని సృజించెను.", "So God created man in his own image, in the image of God he created him."),
            BibleVerse("GEN", "ఆదికాండము", "Genesis", 1, 31, "దేవుడు తాను చేసినది అంతయు చూచినప్పుడు అది మిక్కిలి మంచిదిగా ఉండెను. అస్తమయమును ఉదయమును కలుగగా ఆరవ దినమాయెను.", "And God saw everything that he had made, and behold, it was very good.")
        ),

        // Psalms 23 (కీర్తన 23)
        "PSA_23" to listOf(
            BibleVerse("PSA", "కీర్తనలు", "Psalms", 23, 1, "యెహోవా నా కాపరి; నాకు లేమి కలుగదు.", "The LORD is my shepherd; I shall not want."),
            BibleVerse("PSA", "కీర్తనలు", "Psalms", 23, 2, "పచ్చికగల చోట్లను ఆయన నన్ను పరుండజేయుచున్నాడు; శాంతికరమైన జలములయొద్ద నన్ను నడిపించుచున్నాడు.", "He makes me lie down in green pastures. He leads me beside still waters."),
            BibleVerse("PSA", "కీర్తనలు", "Psalms", 23, 3, "ఆయన నా ప్రాణమునకు సేదదీర్చుచున్నాడు; తన నామమునుబట్టి నన్ను నీతిమార్గములలో నడిపించుచున్నాడు.", "He restores my soul. He leads me in paths of righteousness for his name's sake."),
            BibleVerse("PSA", "కీర్తనలు", "Psalms", 23, 4, "గాఢాంధకారపు లోయలో నేను సంచరించినను ఏ అపాయమునకు భయపడను, నీవు నాకు తోడై యుందువు; నీ దుడ్డుకఱ్ఱయు నీ దండమును నన్ను ఆదరించును.", "Even though I walk through the valley of the shadow of death, I will fear no evil, for you are with me."),
            BibleVerse("PSA", "కీర్తనలు", "Psalms", 23, 5, "నా శత్రువుల యెదుట నీవు నాకు భోజనము సిద్ధపరచుదువు; నూనెతో నా తల అంటియున్నావు నా గిన్నె నిండి పొర్లుచున్నది.", "You prepare a table before me in the presence of my enemies; you anoint my head with oil; my cup overflows."),
            BibleVerse("PSA", "కీర్తనలు", "Psalms", 23, 6, "నేను బ్రదుకు దినములన్నియు కృపాక్షేమములే నా వెంట వచ్చును; చిరకాలము యెహోవా మందిరములో నేను నివాసము చేసెదను.", "Surely goodness and mercy shall follow me all the days of my life, and I shall dwell in the house of the LORD forever.")
        ),

        // Psalms 91 (కీర్తన 91)
        "PSA_91" to listOf(
            BibleVerse("PSA", "కీర్తనలు", "Psalms", 91, 1, "మహోన్నతుని చాటున నివసించువాడే సర్వశక్తుని నీడను విశ్రమించువాడు.", "He who dwells in the shelter of the Most High will abide in the shadow of the Almighty."),
            BibleVerse("PSA", "కీర్తనలు", "Psalms", 91, 2, "ఆయనే నాకు ఆశ్రయము నా కోట నేను నమ్ముకొను నా దేవుడు అని నేను యెహోవానుగూర్చి పలుకుచున్నాను.", "I will say to the LORD, 'My refuge and my fortress, my God, in whom I trust.'"),
            BibleVerse("PSA", "కీర్తనలు", "Psalms", 91, 3, "వేటకాని యురిలోనుండి ఆయన నిన్ను విడిపించును; నాశనకరమైన తెగులు రాకుండ నిన్ను రక్షించును.", "For he will deliver you from the snare of the fowler and from the deadly pestilence."),
            BibleVerse("PSA", "కీర్తనలు", "Psalms", 91, 4, "ఆయన తన రెక్కలతో నిన్ను కప్పును ఆయన రెక్కలక్రింద నీకు ఆశ్రయము కలుగును; ఆయన సత్యము కేడెమును డాలునై యున్నది.", "He will cover you with his pinions, and under his wings you will find refuge."),
            BibleVerse("PSA", "కీర్తనలు", "Psalms", 91, 11, "నీ మార్గములన్నిటిలో నిన్ను కాపాడుటకు ఆయన నిన్నుగూర్చి తన దూతలకు ఆజ్ఞాపించును.", "For he will command his angels concerning you to guard you in all your ways."),
            BibleVerse("PSA", "కీర్తనలు", "Psalms", 91, 15, "అతడు నాకు మొఱ్ఱపెట్టగా నేను అతనికి ఉత్తరమిచ్చెదను; శ్రమలో నేను అతనికి తోడై యుండెదను; అతని విడిపించి అతని గొప్పచేసెదను.", "When he calls to me, I will answer him; I will be with him in trouble.")
        ),

        // Psalms 121 (కీర్తన 121)
        "PSA_121" to listOf(
            BibleVerse("PSA", "కీర్తనలు", "Psalms", 121, 1, "కొండలతట్టు నా కన్నులెత్తుచున్నాను; నాకు సహాయము ఎక్కడనుండి వచ్చును?", "I lift up my eyes to the hills. From where does my help come?"),
            BibleVerse("PSA", "కీర్తనలు", "Psalms", 121, 2, "భూమ్యాకాశములను సృజించిన యెహోవావలననే నాకు సహాయము కలుగును.", "My help comes from the LORD, who made heaven and earth."),
            BibleVerse("PSA", "కీర్తనలు", "Psalms", 121, 3, "ఆయన నీ పాదము తొట్రిల్లనీయడు; నిన్ను కాపాడువాడు కునుకడు.", "He will not let your foot be moved; he who keeps you will not slumber."),
            BibleVerse("PSA", "కీర్తనలు", "Psalms", 121, 5, "యెహోవాయే నిన్ను కాపాడువాడు; నీ కుడిచేతి ప్రక్కన యెహోవా నీకు నీడగా ఉండును.", "The LORD is your keeper; the LORD is your shade on your right hand."),
            BibleVerse("PSA", "కీర్తనలు", "Psalms", 121, 8, "ఇది మొదలుకొని నిరంతరము నీ రాకపోకలయందు యెహోవా నిన్ను కాపాడును.", "The LORD will keep your going out and your coming in from this time forth and forevermore.")
        ),

        // Proverbs 3 (సామెతలు 3)
        "PRO_3" to listOf(
            BibleVerse("PRO", "సామెతలు", "Proverbs", 3, 5, "నీ స్వబుద్ధిని ఆధారము చేసికొనక నీ పూర్ణహృదయముతో యెహోవాయందు నమ్మకముంచుము.", "Trust in the LORD with all your heart, and do not lean on your own understanding."),
            BibleVerse("PRO", "సామెతలు", "Proverbs", 3, 6, "నీ ప్రవర్తన అంతటియందు ఆయన అధికారమునకు ఒప్పుకొనుము; అప్పుడు ఆయన నీ మార్గములను సరాళము చేయును.", "In all your ways acknowledge him, and he will make straight your paths."),
            BibleVerse("PRO", "సామెతలు", "Proverbs", 3, 7, "నేను జ్ఞానిని గదా అని నీవనుకొనవద్దు; యెహోవాయందు భయభక్తులు కలిగి చెడుతనమును విడిచిపెట్టుము.", "Be not wise in your own eyes; fear the LORD, and turn away from evil."),
            BibleVerse("PRO", "సామెతలు", "Proverbs", 3, 13, "జ్ఞానము సంపాదించినవాడు ధన్యుడు; వివేచన పొందిన నరుడు ధన్యుడు.", "Blessed is the one who finds wisdom, and the one who gets understanding.")
        ),

        // Matthew 5 (మత్తయి 5 - కొండమీది ప్రసంగము)
        "MAT_5" to listOf(
            BibleVerse("MAT", "మత్తయి", "Matthew", 5, 3, "ఆత్మవిషయమై దీనులైనవారు ధన్యులు; పరలోకరాజ్యము వారిది.", "Blessed are the poor in spirit, for theirs is the kingdom of heaven."),
            BibleVerse("MAT", "మత్తయి", "Matthew", 5, 4, "దుఃఖపడువారు ధన్యులు; వారు ఓదార్చబడుదురు.", "Blessed are those who mourn, for they shall be comforted."),
            BibleVerse("MAT", "మత్తయి", "Matthew", 5, 5, "సాత్వికులు ధన్యులు; వారు భూలోకమును స్వతంత్రించుకొందురు.", "Blessed are the meek, for they shall inherit the earth."),
            BibleVerse("MAT", "మత్తయి", "Matthew", 5, 6, "నీతికొరకు ఆకలిదప్పులు గలవారు ధన్యులు; వారు తృప్తిపరచబడుదురు.", "Blessed are those who hunger and thirst for righteousness, for they shall be satisfied."),
            BibleVerse("MAT", "మత్తయి", "Matthew", 5, 7, "దయగలవారు ధన్యులు; వారు దయపొందుదురు.", "Blessed are the merciful, for they shall receive mercy."),
            BibleVerse("MAT", "మత్తయి", "Matthew", 5, 8, "హృదయశుద్ధిగలవారు ధన్యులు; వారు దేవుని చూచెదరు.", "Blessed are the pure in heart, for they shall see God."),
            BibleVerse("MAT", "మత్తయి", "Matthew", 5, 9, "సమాధానపరచువారు ధన్యులు; వారు దేవుని కుమారులనబడుదురు.", "Blessed are the peacemakers, for they shall be called sons of God."),
            BibleVerse("MAT", "మత్తయి", "Matthew", 5, 14, "మీరు లోకమునకు వెలుగై యున్నారు; కొండమీద ఉండు పట్టణము మరుగై యుండనేరదు.", "You are the light of the world. A city set on a hill cannot be hidden."),
            BibleVerse("MAT", "మత్తయి", "Matthew", 5, 16, "మనుష్యులు మీ సత్క్రియలను చూచి పరలోకమందున్న మీ తండ్రిని మహిమపరచునట్లు వారియెదుట మీ వెలుగు ప్రకాశింపనీయుడి.", "In the same way, let your light shine before others.")
        ),

        // Matthew 6 (మత్తయి 6 - పరలోక ప్రార్థన & చింతించకుడి)
        "MAT_6" to listOf(
            BibleVerse("MAT", "మత్తయి", "Matthew", 6, 9, "కాబట్టి మీరీలాగు ప్రార్థన చేయుడి—పరలోకమందున్న మా తండ్రీ, నీ నామము పరిశుద్ధపరచబడును గాక;", "Pray then like this: 'Our Father in heaven, hallowed be your name.'"),
            BibleVerse("MAT", "మత్తయి", "Matthew", 6, 10, "నీ రాజ్యము వచ్చును గాక; నీ చిత్తము పరలోకమందు నెరవేరుచున్నట్లు భూమియందును నెరవేరును గాక;", "Your kingdom come, your will be done, on earth as it is in heaven."),
            BibleVerse("MAT", "మత్తయి", "Matthew", 6, 11, "మా దినాహారము నేడు మాకు దయచేయుము;", "Give us this day our daily bread,"),
            BibleVerse("MAT", "మత్తయి", "Matthew", 6, 12, "మా ఋణస్థులను మేము క్షమించియున్న ప్రకారము మా ఋణములను క్షమించుము;", "and forgive us our debts, as we also have forgiven our debtors."),
            BibleVerse("MAT", "మత్తయి", "Matthew", 6, 13, "మమ్మును శోధనలోనికి తేక దుష్టునినుండి మమ్మును తప్పించుము; రాజ్యమును బలమును మహిమయు నిరంతరము నీవై యున్నవి. ఆమేన్.", "And lead us not into temptation, but deliver us from evil."),
            BibleVerse("MAT", "మత్తయి", "Matthew", 6, 33, "కాబట్టి మీరు ఆయన రాజ్యమును నీతిని మొదట వెదకుడి; అప్పుడు అవన్నియు మీకనుగ్రహింపబడును.", "But seek first the kingdom of God and his righteousness, and all these things will be added to you.")
        ),

        // John 1 (యోహాను 1)
        "JHN_1" to listOf(
            BibleVerse("JHN", "యోహాను", "John", 1, 1, "ఆదియందు వాక్యము ఉండెను; వాక్యము దేవునియొద్ద ఉండెను; వాక్యము దేవుడై యుండెను.", "In the beginning was the Word, and the Word was with God, and the Word was God."),
            BibleVerse("JHN", "యోహాను", "John", 1, 2, "ఆయన ఆదియందు దేవునియొద్ద ఉండెను.", "He was in the beginning with God."),
            BibleVerse("JHN", "యోహాను", "John", 1, 3, "సమస్తమును ఆయన మూలముగా కలిగెను; కలిగియున్నదేదియు ఆయన లేకుండ కలుగలేదు.", "All things were made through him, and without him was not any thing made that was made."),
            BibleVerse("JHN", "యోహాను", "John", 1, 4, "ఆయనలో జీవము ఉండెను; ఆ జీవము మనుష్యులకు వెలుగై యుండెను.", "In him was life, and the life was the light of men."),
            BibleVerse("JHN", "యోహాను", "John", 1, 12, "తన్ను ఎందరంగీకరించిరో, అనగా తన నామమునందు విశ్వాసముంచినవారికి, దేవుని పిల్లలగుటకు ఆయన అధికారము ఇచ్చెను.", "But to all who did receive him, who believed in his name, he gave the right to become children of God."),
            BibleVerse("JHN", "యోహాను", "John", 1, 14, "ఆ వాక్యము శరీరధారియై, కృపాసత్యసంపూర్ణుడుగా మనమధ్య నివసించెను; తండ్రివలన కలిగిన అద్వితీయకుమారుని మహిమవలె మనము ఆయన మహిమను కనుగొంటిమి.", "And the Word became flesh and dwelt among us, and we have seen his glory.")
        ),

        // John 3 (యోహాను 3)
        "JHN_3" to listOf(
            BibleVerse("JHN", "యోహాను", "John", 3, 16, "దేవుడు లోకమును ఎంతో ప్రేమించెను. కాగా ఆయన తన అద్వితీయకుమారునిగా పుట్టిన వానియందు విశ్వాసముంచు ప్రతివాడును నశింపక నిత్యజీవము పొందునట్లు ఆయనను అనుగ్రహించెను.", "For God so loved the world, that he gave his only Son, that whoever believes in him should not perish but have eternal life."),
            BibleVerse("JHN", "యోహాను", "John", 3, 17, "లోకము తన కుమారుని ద్వారా రక్షణ పొందుటకే గాని లోకమునకు తీర్పు తీర్చుటకు దేవుడు ఆయనను లోకములోనికి పంపలేదు.", "For God did not send his Son into the world to condemn the world, but in order that the world might be saved through him.")
        ),

        // John 14 (యోహాను 14)
        "JHN_14" to listOf(
            BibleVerse("JHN", "యోహాను", "John", 14, 1, "మీ హృదయమును కలవరపడనియ్యకుడి; దేవునియందు విశ్వాసముంచుచున్నారు నాయందును విశ్వాసముంచుడి.", "Let not your hearts be troubled. Believe in God; believe also in me."),
            BibleVerse("JHN", "యోహాను", "John", 14, 6, "యేసు—నేనే మార్గమును, సత్యమును, జీవమును; నా ద్వారానే తప్ప యెవడును తండ్రియొద్దకు రాడు.", "Jesus said to him, 'I am the way, and the truth, and the life. No one comes to the Father except through me.'"),
            BibleVerse("JHN", "యోహాను", "John", 14, 27, "శాంతి మీకొరకు విడిచి వెళ్లుచున్నాను, నా శాంతినే మీకు అనుగ్రహించుచున్నాను; లోకమిచ్చునట్టుగా నేను మీకు అనుగ్రహించుటలేదు. మీ హృదయమును కలవరపడనియ్యకుడి, వెరవనియ్యకుడి.", "Peace I leave with you; my peace I give to you.")
        ),

        // Romans 8 (రోమీయులకు 8)
        "ROM_8" to listOf(
            BibleVerse("ROM", "రోమీయులకు", "Romans", 8, 1, "కాబట్టి ఇప్పుడు క్రీస్తుయేసునందున్నవారికి ఏ శిక్షావిధియు లేదు.", "There is therefore now no condemnation for those who are in Christ Jesus."),
            BibleVerse("ROM", "రోమీయులకు", "Romans", 8, 28, "దేవుని ప్రేమించువారికి, అనగా ఆయన సంకల్పముచొప్పున పిలువబడినవారికి, సమస్తమును సమకూడి మేలుకొరకే జరుగుచున్నవని యెరుగుదుము.", "And we know that for those who love God all things work together for good."),
            BibleVerse("ROM", "రోమీయులకు", "Romans", 8, 31, "ఇట్లుండగా ఏమందుము? దేవుడు మనపక్షముననుండగా మనకు విరోధియెవడు?", "What then shall we say to these things? If God is for us, who can be against us?"),
            BibleVerse("ROM", "రోమీయులకు", "Romans", 8, 38, "మరణమైనను జీవమైనను దేవదూతలైనను ప్రధానులైనను ఉన్నవియైనను రాబోవునవియైనను అధికారులైనను,", "For I am sure that neither death nor life, nor angels nor rulers, nor things present nor things to come,"),
            BibleVerse("ROM", "రోమీయులకు", "Romans", 8, 39, "ఎత్తైనను లోతైనను మరే సృష్టియైనను, మన ప్రభువైన క్రీస్తుయేసునందలి దేవుని ప్రేమనుండి మనలను ఎడబాపనేరవని రూఢిగా నమ్ముచున్నాను.", "nor height nor depth, nor anything else in all creation, will be able to separate us from the love of God in Christ Jesus our Lord.")
        ),

        // 1 Corinthians 13 (1 కొరింథీయులకు 13 - ప్రేమ అధ్యాయము)
        "1CO_13" to listOf(
            BibleVerse("1CO", "1 కొరింథీయులకు", "1 Corinthians", 13, 4, "ప్రేమ దీర్ఘకాలము సహించును, దయ చూపించును; ప్రేమ మత్సరపడదు; ప్రేమ డంబముగా ప్రవర్తింపదు, ఉప్పొంగదు.", "Love is patient and kind; love does not envy or boast; it is not arrogant"),
            BibleVerse("1CO", "1 కొరింథీయులకు", "1 Corinthians", 13, 7, "అన్నిటిని తాళుకొనును, అన్నిటిని నమ్మును, అన్నిటిని నిరీక్షించును, అన్నిటిని ఓర్చును.", "Love bears all things, believes all things, hopes all things, endures all things."),
            BibleVerse("1CO", "1 కొరింథీయులకు", "1 Corinthians", 13, 8, "ప్రేమ ఎన్నటికిని గతించిపోదు.", "Love never ends."),
            BibleVerse("1CO", "1 కొరింథీయులకు", "1 Corinthians", 13, 13, "కాగా నిలుచునవి విశ్వాసము, నిరీక్షణ, ప్రేమ యను ఈ మూడును; వీటిలో శ్రేష్ఠమైనది ప్రేమయే.", "So now faith, hope, and love abide, these three; but the greatest of these is love.")
        ),

        // Philippians 4 (ఫిలిప్పీయులకు 4)
        "PHP_4" to listOf(
            BibleVerse("PHP", "ఫిలిప్పీయులకు", "Philippians", 4, 4, "ఎల్లప్పుడును ప్రభువునందు ఆనందించుడి, మరల చెప్పుదును ఆనందించుడి.", "Rejoice in the Lord always; again I will say, rejoice."),
            BibleVerse("PHP", "ఫిలిప్పీయులకు", "Philippians", 4, 6, "దేనినిగూర్చియు చింతపడకుడి గాని ప్రతి విషయములోను ప్రార్థన విజ్ఞాపనములచేత కృతజ్ఞతాపూర్వకముగా మీ విన్నపములు దేవునికి తెలియజేయుడి.", "Do not be anxious about anything, but in everything by prayer and supplication with thanksgiving let your requests be made known to God."),
            BibleVerse("PHP", "ఫిలిప్పీయులకు", "Philippians", 4, 7, "అప్పుడు సమస్త జ్ఞానమునకు మించిన దేవుని సమాధానము యేసుక్రీస్తువలన మీ హృదయములకును మీ తలంపులకును కావలియుండును.", "And the peace of God, which surpasses all understanding, will guard your hearts and your minds in Christ Jesus."),
            BibleVerse("PHP", "ఫిలిప్పీయులకు", "Philippians", 4, 13, "నన్ను బలపరచువానియందే నేను సమస్తమును చేయగలను.", "I can do all things through him who strengthens me.")
        ),

        // Revelation 21 (ప్రకటన 21)
        "REV_21" to listOf(
            BibleVerse("REV", "ప్రకటన గ్రంథము", "Revelation", 21, 1, "అంతట నేను క్రొత్త ఆకాశమును క్రొత్త భూమిని చూచితిని. మొదటి ఆకాశమును మొదటి భూమియు గతించిపోయెను, సముద్రమును ఇకను లేదు.", "Then I saw a new heaven and a new earth, for the first heaven and the first earth had passed away."),
            BibleVerse("REV", "ప్రకటన గ్రంథము", "Revelation", 21, 3, "ఇదిగో దేవుని నివాసము మనుష్యులతో ఉన్నది, ఆయన వారితో కాపురముండును, వారు ఆయన ప్రజలైయుందురు.", "Behold, the dwelling place of God is with man. He will dwell with them, and they will be his people."),
            BibleVerse("REV", "ప్రకటన గ్రంథము", "Revelation", 21, 4, "ఆయన వారి కన్నుల ప్రతి బాష్పబిందువును తుడిచివేయును, మరణము ఇక ఉండదు, దుఃఖమైనను ఏడ్పైనను వేదనయైనను ఇక ఉండదు.", "He will wipe away every tear from their eyes, and death shall be no more, neither shall there be mourning, nor crying, nor pain anymore.")
        )
    )

    fun getVerses(bookId: String, chapter: Int): List<BibleVerse> {
        val key = "${bookId}_$chapter"
        val existing = versesDatabase[key]
        if (existing != null) return existing

        val book = books.find { it.id == bookId } ?: books.first()
        // Provide standard chapter verses placeholder if chapter is extended
        return listOf(
            BibleVerse(book.id, book.nameTelugu, book.nameEnglish, chapter, 1, "${book.nameTelugu} అధ్యాయము $chapter: పరిశుద్ధ గ్రంథ వాక్యము.", "Chapter $chapter verse 1."),
            BibleVerse(book.id, book.nameTelugu, book.nameEnglish, chapter, 2, "ప్రభువు వాక్యము నా పాదములకు దీపమును, నా త్రోవకు వెలుగునై యున్నది.", "Your word is a lamp to my feet and a light to my path."),
            BibleVerse(book.id, book.nameTelugu, book.nameEnglish, chapter, 3, "దేవుని యందు విశ్వాసముంచుడి, ఆయన మీ భారములను భరించును.", "Trust in the Lord, and he will sustain you."),
            BibleVerse(book.id, book.nameTelugu, book.nameEnglish, chapter, 4, "శాంతి సమాధానములు మీతో ఉండును గాక. దేవుని వాక్యము సత్యమైనది.", "Peace be with you. The word of God is true."),
            BibleVerse(book.id, book.nameTelugu, book.nameEnglish, chapter, 5, "ఆయన నామము నిరంతరము స్తోత్రార్హమైనది. ఆమేన్.", "His name is worthy of praise forever. Amen.")
        )
    }

    val dailyVerses: List<DailyVerse> = listOf(
        DailyVerse(
            title = "నేటి వాక్యము • దేవుని రక్షణ",
            reference = "యోహాను 3:16",
            textTelugu = "దేవుడు లోకమును ఎంతో ప్రేమించెను. కాగా ఆయన తన అద్వితీయకుమారునిగా పుట్టిన వానియందు విశ్వాసముంచు ప్రతివాడును నశింపక నిత్యజీవము పొందునట్లు ఆయనను అనుగ్రహించెను.",
            textEnglish = "For God so loved the world, that he gave his only Son, that whoever believes in him should not perish but have eternal life.",
            devotionalThought = "దేవుని అమూల్యమైన ప్రేమ మనకు నిత్యజీవమును ప్రసాదించినది. నేడు ఆ ప్రేమను ఇతరులతో పంచుకుందాం."
        ),
        DailyVerse(
            title = "నేటి వాక్యము • సంరక్షణ & భద్రత",
            reference = "కీర్తనలు 23:1",
            textTelugu = "యెహోవా నా కాపరి; నాకు లేమి కలుగదు.",
            textEnglish = "The LORD is my shepherd; I shall not want.",
            devotionalThought = "ప్రభువు మనలను నడిపించే మంచి కాపరి. ఏ లోటు మనలను కృంగదీయలేదు."
        ),
        DailyVerse(
            title = "నేటి వాక్యము • దైవిక జ్ఞానము",
            reference = "సామెతలు 3:5-6",
            textTelugu = "నీ స్వబుద్ధిని ఆధారము చేసికొనక నీ పూర్ణహృదయముతో యెహోవాయందు నమ్మకముంచుము. నీ ప్రవర్తన అంతటియందు ఆయన అధికారమునకు ఒప్పుకొనుము; అప్పుడు ఆయన నీ మార్గములను సరాళము చేయును.",
            textEnglish = "Trust in the LORD with all your heart, and do not lean on your own understanding.",
            devotionalThought = "మీ అన్ని నిర్ణయాలలో దేవుని చిత్తానికి మొదటి స్థానమివ్వండి."
        ),
        DailyVerse(
            title = "నేటి వాక్యము • ధైర్యము & బలం",
            reference = "ఫిలిప్పీయులకు 4:13",
            textTelugu = "నన్ను బలపరచువానియందే నేను సమస్తమును చేయగలను.",
            textEnglish = "I can do all things through him who strengthens me.",
            devotionalThought = "క్రీస్తు ఇచ్చే ఆత్మ బలంతో నేటి సవాళ్లన్నిటినీ మీరు అధిగమించగలరు."
        ),
        DailyVerse(
            title = "నేటి వాక్యము • శాంతి & సమాధానము",
            reference = "యోహాను 14:27",
            textTelugu = "శాంతి మీకొరకు విడిచి వెళ్లుచున్నాను, నా శాంతినే మీకు అనుగ్రహించుచున్నాను; లోకమిచ్చునట్టుగా నేను మీకు అనుగ్రహించుటలేదు. మీ హృదయమును కలవరపడనియ్యకుడి, వెరవనియ్యకుడి.",
            textEnglish = "Peace I leave with you; my peace I give to you. Not as the world gives do I give to you.",
            devotionalThought = "లోకపు కలవరాల మధ్య క్రీస్తు ఇచ్చే దైవిక సమాధానము మన హృదయాలను కాపాడుతుంది."
        ),
        DailyVerse(
            title = "నేటి వాక్యము • అద్భుత నిరీక్షణ",
            reference = "రోమీయులకు 8:28",
            textTelugu = "దేవుని ప్రేమించువారికి, అనగా ఆయన సంకల్పముచొప్పున పిలువబడినవారికి, సమస్తమును సమకూడి మేలుకొరకే జరుగుచున్నవని యెరుగుదుము.",
            textEnglish = "And we know that for those who love God all things work together for good.",
            devotionalThought = "ప్రతి పరిస్థితిని దేవుడు మన మేలుకొరకే మలుచును."
        )
    )

    fun getTodayVerse(): DailyVerse {
        val dayIndex = (System.currentTimeMillis() / (1000 * 60 * 60 * 24) % dailyVerses.size).toInt()
        return dailyVerses[dayIndex]
    }

    // 30-Day New Testament Reading Plan Portions
    val thirtyDayPlanItems: List<ReadingDayItem> = (1..30).map { day ->
        when (day) {
            1 -> ReadingDayItem(1, "సువార్తల ఆరంభం - మత్తయి జననం", listOf("మత్తయి 1", "మత్తయి 2"), "మత్తయి 1:21")
            2 -> ReadingDayItem(2, "బాప్తిస్మము & శోధనలు", listOf("మత్తయి 3", "మత్తయి 4"), "మత్తయి 4:4")
            3 -> ReadingDayItem(3, "కొండమీది ప్రసంగము భాగం 1", listOf("మత్తయి 5"), "మత్తయి 5:14")
            4 -> ReadingDayItem(4, "కొండమీది ప్రసంగము భాగం 2", listOf("మత్తయి 6", "మత్తయి 7"), "మత్తయి 6:33")
            5 -> ReadingDayItem(5, "యేసు అద్భుతాలు & స్వస్థతలు", listOf("మత్తయి 8", "మత్తయి 9"), "మత్తయి 9:36")
            6 -> ReadingDayItem(6, "యోహాను సువార్త - వాక్య మహిమ", listOf("యోహాను 1", "యోహాను 2"), "యోహాను 1:14")
            7 -> ReadingDayItem(7, "క్రొత్త జన్మము & నిత్యజీవము", listOf("యోహాను 3", "యోహాను 4"), "యోహాను 3:16")
            8 -> ReadingDayItem(8, "జీవాహారము & మంచి కాపరి", listOf("యోహాను 6", "యోహాను 10"), "యోహాను 10:11")
            9 -> ReadingDayItem(9, "పునరుత్థానము & జీవము", listOf("యోహాను 11", "యోహాను 12"), "యోహాను 11:25")
            10 -> ReadingDayItem(10, "శాంతి సందేశం & ద్రాక్షావల్లి", listOf("యోహాను 14", "యోహాను 15"), "యోహాను 14:6")
            11 -> ReadingDayItem(11, "పరిశుద్ధాత్మ వాగ్దానము", listOf("అపొస్తలుల 1", "అపొస్తలుల 2"), "అపొస్తలుల 1:8")
            12 -> ReadingDayItem(12, "ప్రారంభ సంఘ విశ్వాస యాత్ర", listOf("అపొస్తలుల 3", "అపొస్తలుల 4"), "అపొస్తలుల 4:12")
            13 -> ReadingDayItem(13, "పౌలు పరివర్తన & సువార్త వ్యాప్తి", listOf("అపొస్తలుల 9"), "అపొస్తలుల 9:15")
            14 -> ReadingDayItem(14, "రోమీయులకు - కృప ద్వారా నీతిమంతులుగా", listOf("రోమీయులకు 1", "రోమీయులకు 3"), "రోమీయులకు 1:16")
            15 -> ReadingDayItem(15, "విశ్వాసము వలన సమాధానము", listOf("రోమీయులకు 5", "రోమీయులకు 6"), "రోమీయులకు 5:1")
            16 -> ReadingDayItem(16, "ఆత్మ జీవితము & జయము", listOf("రోమీయులకు 8"), "రోమీయులకు 8:31")
            17 -> ReadingDayItem(17, "సజీవ యాగముగా మన దేహములు", listOf("రోమీయులకు 12"), "రోమీయులకు 12:2")
            18 -> ReadingDayItem(18, "శ్రేష్ఠమైన ప్రేమ మార్గము", listOf("1 కొరింథీయులకు 13"), "1 కొరింథీయులకు 13:13")
            19 -> ReadingDayItem(19, "పునరుత్థాన విజయము", listOf("1 కొరింథీయులకు 15"), "1 కొరింథీయులకు 15:57")
            20 -> ReadingDayItem(20, "ఆత్మ ఫలములు", listOf("గలతీయులకు 5"), "గలతీయులకు 5:22")
            21 -> ReadingDayItem(21, "కృప చేతనే రక్షణ", listOf("ఎఫెసీయులకు 2"), "ఎఫెసీయులకు 2:8")
            22 -> ReadingDayItem(22, "సర్వాంగ కవచము ధరించుకొనుడి", listOf("ఎఫెసీయులకు 6"), "ఎఫెసీయులకు 6:11")
            23 -> ReadingDayItem(23, "ప్రభువునందు ఆనందించుడి", listOf("ఫిలిప్పీయులకు 4"), "ఫిలిప్పీయులకు 4:4")
            24 -> ReadingDayItem(24, "క్రీస్తు సర్వోన్నత మహిమ", listOf("కొలొస్సయులకు 1"), "కొలొస్సయులకు 1:18")
            25 -> ReadingDayItem(25, "విశ్వాస వీరుల సాక్ష్యము", listOf("హెబ్రీయులకు 11"), "హెబ్రీయులకు 11:1")
            26 -> ReadingDayItem(26, "విశ్వాసమును క్రియలలో చూపుట", listOf("యాకోబు 1", "యాకోబు 2"), "యాకోబు 1:22")
            27 -> ReadingDayItem(27, "జీవముగల నిరీక్షణ", listOf("1 పేతురు 1", "1 పేతురు 2"), "1 పేతురు 1:3")
            28 -> ReadingDayItem(28, "దేవుడు ప్రేమాస్వరూపి", listOf("1 యోహాను 4"), "1 యోహాను 4:8")
            29 -> ReadingDayItem(29, "క్రొత్త యెరూషలేము దర్శనము", listOf("ప్రకటన 21"), "ప్రకటన 21:4")
            else -> ReadingDayItem(30, "ప్రభువైన యేసు త్వరగా వచ్చుచున్నాడు", listOf("ప్రకటన 22"), "ప్రకటన 22:20")
        }
    }

    fun searchScripture(query: String): List<BibleVerse> {
        val trimmed = query.trim().lowercase()
        if (trimmed.isEmpty()) return emptyList()

        val results = mutableListOf<BibleVerse>()
        versesDatabase.values.flatten().forEach { verse ->
            if (verse.textTelugu.contains(trimmed, ignoreCase = true) ||
                verse.textEnglish.contains(trimmed, ignoreCase = true) ||
                verse.bookNameTelugu.contains(trimmed, ignoreCase = true) ||
                verse.bookNameEnglish.contains(trimmed, ignoreCase = true) ||
                "${verse.bookNameTelugu} ${verse.chapter}:${verse.verse}".contains(trimmed, ignoreCase = true) ||
                "${verse.bookNameEnglish} ${verse.chapter}:${verse.verse}".contains(trimmed, ignoreCase = true)
            ) {
                results.add(verse)
            }
        }
        return results
    }
}
