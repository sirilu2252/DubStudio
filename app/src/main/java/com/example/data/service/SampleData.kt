package com.example.data.service

import com.example.data.model.EmotionType
import com.example.data.model.Speaker
import com.example.data.model.TranscriptSegment
import com.example.data.model.VideoMetadata

object SampleData {
    val sampleVideos = listOf(
        VideoMetadata(
            title = "Apresentação Tech & Inteligência Artificial",
            durationSec = 28f,
            resolution = "1080p (1080x1920)",
            format = "MP4",
            fileSizeBytes = 34_600_000,
            fps = 60,
            sampleId = "sample_tech"
        ),
        VideoMetadata(
            title = "Podcast Creators: Conversa entre 2 Locutores",
            durationSec = 36f,
            resolution = "4K UHD (2160x3840)",
            format = "MOV",
            fileSizeBytes = 78_200_000,
            fps = 30,
            sampleId = "sample_podcast"
        ),
        VideoMetadata(
            title = "Masterclass de Gastronomia & Receita",
            durationSec = 22f,
            resolution = "1080p (1920x1080)",
            format = "WebM",
            fileSizeBytes = 24_100_000,
            fps = 30,
            sampleId = "sample_cooking"
        )
    )

    fun getSampleSpeakers(sampleId: String): List<Speaker> {
        return when (sampleId) {
            "sample_podcast" -> listOf(
                Speaker(
                    id = "spk_1",
                    name = "Pessoa 1 (Apresentador)",
                    assignedVoiceName = "Voz Clonal do Usuário",
                    gender = "Masculino",
                    pitchMultiplier = 1.0f,
                    speedMultiplier = 1.0f,
                    isCustomVoice = true,
                    avatarColorHex = 0xFF8B5CF6
                ),
                Speaker(
                    id = "spk_2",
                    name = "Pessoa 2 (Convidada)",
                    assignedVoiceName = "Elena Pro Studio",
                    gender = "Feminino",
                    pitchMultiplier = 1.15f,
                    speedMultiplier = 0.98f,
                    isCustomVoice = false,
                    avatarColorHex = 0xFFEC4899
                )
            )
            "sample_cooking" -> listOf(
                Speaker(
                    id = "spk_1",
                    name = "Chef Marco",
                    assignedVoiceName = "Marco Espresso",
                    gender = "Masculino",
                    pitchMultiplier = 0.95f,
                    speedMultiplier = 1.05f,
                    isCustomVoice = false,
                    avatarColorHex = 0xFFF59E0B
                )
            )
            else -> listOf(
                Speaker(
                    id = "spk_1",
                    name = "Locutor Principal",
                    assignedVoiceName = "Minha Voz (Clone Autorizado)",
                    gender = "Masculino",
                    pitchMultiplier = 1.0f,
                    speedMultiplier = 1.0f,
                    isCustomVoice = true,
                    avatarColorHex = 0xFF7C4DFF
                )
            )
        }
    }

    fun getSampleSegments(sampleId: String, targetLangCode: String): List<TranscriptSegment> {
        val isEn = targetLangCode.lowercase() == "en"
        val isEs = targetLangCode.lowercase() == "es"
        val isFr = targetLangCode.lowercase() == "fr"

        return when (sampleId) {
            "sample_podcast" -> listOf(
                TranscriptSegment(
                    id = "seg_pod_1",
                    startTimeSec = 0.0f,
                    endTimeSec = 5.2f,
                    speakerId = "spk_1",
                    originalText = "Fala galera! Bem-vindos de volta ao podcast. Hoje a gente vai falar sobre criação de conteúdo com IA.",
                    directTranslation = if (isEn) "Speak guys! Welcome back to podcast. Today we people will speak about creation of content with AI."
                    else if (isEs) "¡Habla chicos! Bienvenidos de vuelta al podcast. Hoy nosotros vamos a hablar sobre creación de contenido con IA."
                    else "Salut les gars ! Bienvenue sur le podcast. Aujourd'hui nous parlons de création de contenu IA.",
                    revisedTranslation = if (isEn) "Hey everyone! Welcome back to the show. Today we're diving deep into AI content creation."
                    else if (isEs) "¡Hola a todos! Bienvenidos de nuevo al programa. Hoy profundizaremos en la creación de contenido con IA."
                    else "Salut tout le monde ! Bienvenue. Aujourd'hui on plonge dans la création de contenu avec l'IA.",
                    dubbingScript = if (isEn) "Hey everyone! Welcome back to the show. Today we're diving deep into AI content creation."
                    else if (isEs) "¡Hola a todos! Bienvenidos de nuevo al programa. Hoy exploramos el contenido con IA."
                    else "Salut à tous ! Bienvenue. Aujourd'hui on plonge dans la création avec l'IA.",
                    emotion = EmotionType.ENTHUSIASTIC,
                    speedWpm = 152,
                    lipSyncConfidence = 98,
                    linguisticNotes = "Expressão informal 'Fala galera' adaptada para o natural 'Hey everyone' / '¡Hola a todos'."
                ),
                TranscriptSegment(
                    id = "seg_pod_2",
                    startTimeSec = 5.3f,
                    endTimeSec = 11.8f,
                    speakerId = "spk_2",
                    originalText = "Totalmente! E o mais insano é como a sincronização labial consegue manter o tom emocional sem parecer fake.",
                    directTranslation = if (isEn) "Totally! And the most insane is how the lip sync manages to keep emotional tone without looking fake."
                    else if (isEs) "¡Totalmente! Y lo más loco es cómo la sincronización labial logra mantener el tono emocional sin parecer falso."
                    else "Totalement ! Et le plus fou c'est comment la synchro labiale garde le ton sans paraître fausse.",
                    revisedTranslation = if (isEn) "Absolutely! And what's truly mind-blowing is how the lip sync preserves your emotional nuance seamlessly."
                    else if (isEs) "¡Totalmente! Y lo más fascinante es cómo la sincronía labial preserva tus matices emocionales sin fisuras."
                    else "Absolument ! Et ce qui est bluffant, c'est la façon dont la synchro labiale préserve vos émotions.",
                    dubbingScript = if (isEn) "Absolutely! And what's mind-blowing is how the lip sync preserves your emotional nuance seamlessly."
                    else if (isEs) "¡Totalmente! Y lo fascinante es cómo la sincronía labial preserva tus emociones con fluidez."
                    else "Absolument ! Ce qui est bluffant, c'est comme la synchro préserve vos émotions.",
                    emotion = EmotionType.SURPRISED,
                    speedWpm = 148,
                    lipSyncConfidence = 96,
                    linguisticNotes = "'Mais insano' adaptado culturalmente para 'mind-blowing' para manter energia entusiasmada."
                ),
                TranscriptSegment(
                    id = "seg_pod_3",
                    startTimeSec = 12.0f,
                    endTimeSec = 19.5f,
                    speakerId = "spk_1",
                    originalText = "Exato, cara. Antigamente você gastava semanas em estúdio com dubladores. Agora você faz em minutos.",
                    directTranslation = if (isEn) "Exact, face. Formerly you spent weeks in studio with dubbers. Now you do in minutes."
                    else if (isEs) "Exacto, cara. Antiguamente gastabas semanas en estudio con dobladores. Ahora haces en minutos."
                    else "Exact, mec. Autrefois tu passais des semaines en studio avec des doubleurs. Maintenant tu fais en minutes.",
                    revisedTranslation = if (isEn) "Exactly, man. We used to spend weeks in high-end dubbing studios. Now it takes just minutes."
                    else if (isEs) "Exacto. Antes pasábamos semanas enteras en estudios de doblaje. Ahora toma solo minutos."
                    else "Exactement. On passait des semaines en studio de doublage. Maintenant ça prend quelques minutes.",
                    dubbingScript = if (isEn) "Exactly. We used to spend weeks in dubbing studios. Now it takes just minutes."
                    else if (isEs) "Exacto. Antes pasábamos semanas en estudios de doblaje. Ahora toma solo minutos."
                    else "Exactement. On passait des semaines en studio. Maintenant ça prend juste quelques minutes.",
                    emotion = EmotionType.MOTIVATIONAL,
                    speedWpm = 140,
                    lipSyncConfidence = 97,
                    linguisticNotes = "Remoção de literalidade de 'cara' e concisão temporal para caber no timing da cena."
                ),
                TranscriptSegment(
                    id = "seg_pod_4",
                    startTimeSec = 19.8f,
                    endTimeSec = 26.5f,
                    speakerId = "spk_2",
                    originalText = "E a melhor parte é que as músicas de fundo e efeitos continuam intocados!",
                    directTranslation = if (isEn) "And the best part is that the background songs and effects continue untouched!"
                    else if (isEs) "¡Y la mejor parte es que las canciones de fondo y efectos continúan intocados!"
                    else "Et la meilleure partie est que les musiques de fond et effets restent intacts !",
                    revisedTranslation = if (isEn) "And best of all, the background music and sound design stay completely untouched!"
                    else if (isEs) "¡Y lo mejor de todo es que la música de fondo y los efectos se mantienen intactos!"
                    else "Et surtout, la musique de fond et le sound design restent totalement intacts !",
                    dubbingScript = if (isEn) "And best of all, background music and sound effects stay completely intact!"
                    else if (isEs) "¡Y lo mejor es que la música de fondo y los efectos quedan intactos!"
                    else "Et le meilleur, c'est que la musique et les effets restent intacts !",
                    emotion = EmotionType.HAPPY,
                    speedWpm = 142,
                    lipSyncConfidence = 99,
                    linguisticNotes = "Refinamento de 'sound design' e timing ajustado para pico rítmico."
                )
            )
            "sample_cooking" -> listOf(
                TranscriptSegment(
                    id = "seg_ck_1",
                    startTimeSec = 0.0f,
                    endTimeSec = 6.0f,
                    speakerId = "spk_1",
                    originalText = "O segredo desse molho é o ponto do alho: dourado de leve, sem queimar.",
                    directTranslation = if (isEn) "The secret of this sauce is garlic point: light golden, no burn."
                    else if (isEs) "El secreto de esta salsa es el punto del ajo: dorado suave, sin quemar."
                    else "Le secret de cette sauce est le point de l'ail : doré léger, sans brûler.",
                    revisedTranslation = if (isEn) "The real secret to this sauce is the garlic: gently golden, never scorched."
                    else if (isEs) "El verdadero secreto de esta salsa es el ajo: apenas dorado, nunca quemado."
                    else "Le vrai secret de cette sauce réside dans l'ail : délicatement doré, jamais brûlé.",
                    dubbingScript = if (isEn) "The real secret to this sauce is the garlic: gently golden, never scorched."
                    else if (isEs) "El gran secreto de esta salsa es el ajo: ligeramente dorado, sin quemarse."
                    else "Le vrai secret de cette sauce, c'est l'ail : délicatement doré, sans brûler.",
                    emotion = EmotionType.CALM,
                    speedWpm = 135,
                    lipSyncConfidence = 95,
                    linguisticNotes = "'Dourado de leve' transformado em 'gently golden' para tom culinário clássico."
                ),
                TranscriptSegment(
                    id = "seg_ck_2",
                    startTimeSec = 6.2f,
                    endTimeSec = 13.5f,
                    speakerId = "spk_1",
                    originalText = "Adicione o azeite extravirgem com calma para emulsionar perfeitamente.",
                    directTranslation = if (isEn) "Add the extra virgin olive oil with calm to emulsify perfectly."
                    else if (isEs) "Añade el aceite de oliva virgen extra con calma para emulsionar perfectamente."
                    else "Ajoutez l'huile d'olive extra vierge avec calme pour émulsionner parfaitement.",
                    revisedTranslation = if (isEn) "Slowly drizzle in the extra virgin olive oil to create a silky emulsion."
                    else if (isEs) "Vierte despacio el aceite de oliva virgen extra para lograr una emulsión perfecta."
                    else "Versez lentement l'huile d'olive extra vierge pour obtenir une émulsion soyeuse.",
                    dubbingScript = if (isEn) "Slowly drizzle in the olive oil to create a smooth, silky emulsion."
                    else if (isEs) "Vierte el aceite de oliva despacio para crear una emulsión perfecta."
                    else "Versez doucement l'huile d'olive pour créer une émulsion soyeuse.",
                    emotion = EmotionType.SERIOUS,
                    speedWpm = 130,
                    lipSyncConfidence = 97,
                    linguisticNotes = "Adaptação de 'com calma' para 'slowly drizzle' (vocabulário técnico gastronômico)."
                )
            )
            else -> listOf(
                TranscriptSegment(
                    id = "seg_1",
                    startTimeSec = 0.0f,
                    endTimeSec = 5.0f,
                    speakerId = "spk_1",
                    originalText = "Olá mundo! Hoje eu vou mostrar para vocês como dublar vídeos com a sua própria voz usando inteligência artificial.",
                    directTranslation = if (isEn) "Hello world! Today I will show to you guys how to dub videos with your own voice using artificial intelligence."
                    else if (isEs) "¡Hola mundo! Hoy les voy a mostrar a ustedes cómo doblar videos con su propia voz usando inteligencia artificial."
                    else "Bonjour le monde ! Aujourd'hui je vais vous montrer comment doubler des vidéos avec votre propre voix grâce à l'IA.",
                    revisedTranslation = if (isEn) "Hello everyone! Today I'm going to show you how to dub any video in your own voice using AI."
                    else if (isEs) "¡Hola a todos! Hoy les enseñaré a doblar cualquier video conservando su propia voz con IA."
                    else "Bonjour à tous ! Aujourd'hui je vais vous montrer comment doubler une vidéo avec votre propre voix par IA.",
                    dubbingScript = if (isEn) "Hey everyone! Today I'll show you how to dub videos in your own voice with AI."
                    else if (isEs) "¡Hola a todos! Hoy les mostraré cómo doblar videos con su propia voz usando IA."
                    else "Bonjour à tous ! Aujourd'hui, découvrez comment doubler vos vidéos avec votre propre voix.",
                    emotion = EmotionType.ENTHUSIASTIC,
                    speedWpm = 150,
                    lipSyncConfidence = 99,
                    linguisticNotes = "Contração 'I'm going to' -> 'I'll' para casamento perfeito com os 5.0s do take de abertura."
                ),
                TranscriptSegment(
                    id = "seg_2",
                    startTimeSec = 5.2f,
                    endTimeSec = 12.0f,
                    speakerId = "spk_1",
                    originalText = "A ferramenta analisa o timbre, a entonação e sincroniza os lábios em tempo real, sem perder a trilha de fundo.",
                    directTranslation = if (isEn) "The tool analyzes timbre, intonation and synchronizes lips in real time, without losing background track."
                    else if (isEs) "La herramienta analiza el timbre, la entonación y sincroniza los labios en tiempo real, sin perder la pista de fondo."
                    else "L'outil analyse le timbre, l'intonation et synchronise les lèvres en temps réel sans perdre la piste audio.",
                    revisedTranslation = if (isEn) "The engine captures your pitch, cadence, and aligns lip motion in real time while preserving the soundtrack."
                    else if (isEs) "El motor captura tu tono, cadencia y sincroniza el movimiento labial en tiempo real preservando la música."
                    else "Le moteur capture votre timbre, votre intonation et aligne les lèvres en temps réel tout en préservant la musique.",
                    dubbingScript = if (isEn) "It captures your pitch, cadence, and matches lip motion in real time while keeping the background music."
                    else if (isEs) "Captura tu tono, cadencia y sincroniza labios en tiempo real preservando la música de fondo."
                    else "Il capture votre timbre et synchronise les lèvres en temps réel en préservant la musique de fond.",
                    emotion = EmotionType.MOTIVATIONAL,
                    speedWpm = 146,
                    lipSyncConfidence = 96,
                    linguisticNotes = "'Sem perder a trilha' refinado para preservação acústica de stems de áudio."
                ),
                TranscriptSegment(
                    id = "seg_3",
                    startTimeSec = 12.3f,
                    endTimeSec = 19.8f,
                    speakerId = "spk_1",
                    originalText = "Isso permite expandir seu conteúdo para o mundo inteiro sem precisar falar outros idiomas fluentemente.",
                    directTranslation = if (isEn) "This allows expand your content to whole world without needing speak other languages fluently."
                    else if (isEs) "Esto permite expandir su contenido para el mundo entero sin necesitar hablar otros idiomas fluidamente."
                    else "Cela permet d'étendre votre contenu au monde entier sans parler d'autres langues couramment.",
                    revisedTranslation = if (isEn) "This unlocks global reach for your content, without requiring you to speak multiple languages."
                    else if (isEs) "Esto desbloquea alcance global para tu contenido, sin necesidad de dominar otros idiomas."
                    else "Cela débloque une portée mondiale pour vos vidéos sans avoir besoin d'être polyglotte.",
                    dubbingScript = if (isEn) "This unlocks global reach for your content, without needing to speak multiple languages."
                    else if (isEs) "Esto desbloquea alcance global para tus videos sin necesidad de hablar otros idiomas."
                    else "Cela offre une portée mondiale à vos vidéos sans avoir besoin de parler plusieurs langues.",
                    emotion = EmotionType.HAPPY,
                    speedWpm = 144,
                    lipSyncConfidence = 97,
                    linguisticNotes = "Ajuste de 'expandir para o mundo inteiro' para a expressão idiomática moderna 'unlocks global reach'."
                ),
                TranscriptSegment(
                    id = "seg_4",
                    startTimeSec = 20.0f,
                    endTimeSec = 26.5f,
                    speakerId = "spk_1",
                    originalText = "Veja como o resultado final soa natural, expressivo e com sincronia impecável!",
                    directTranslation = if (isEn) "Look how the final result sounds natural, expressive and with impeccable sync!"
                    else if (isEs) "¡Mira cómo el resultado final suena natural, expresivo y con sincronía impecable!"
                    else "Regardez comme le résultat final sonne naturel, expressif et avec une synchro impeccable !",
                    revisedTranslation = if (isEn) "Check out how natural and authentic the final delivery sounds, with seamless synchronization!"
                    else if (isEs) "¡Comprueba qué tan natural y expresivo suena el resultado final, con una sincronización impecable!"
                    else "Découvrez comme le résultat final est naturel, expressif et parfaitement synchronisé !",
                    dubbingScript = if (isEn) "Check out how authentic the delivery sounds, with seamless synchronization!"
                    else if (isEs) "¡Mira lo natural que suena el resultado final, con una sincronía impecable!"
                    else "Regardez comme le résultat sonne naturel, avec une synchronisation parfaite !",
                    emotion = EmotionType.ENTHUSIASTIC,
                    speedWpm = 150,
                    lipSyncConfidence = 98,
                    linguisticNotes = "Fechamento dinâmico com ritmo correspondente ao encerramento do vídeo."
                )
            )
        }
    }
}
