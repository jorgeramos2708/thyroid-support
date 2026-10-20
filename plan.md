## Plan de Desarrollo

| Sprint | Área | Tareas | Duración (hrs) |
|--------|------|--------|----------------|
| **Sprint 1** | Estructura y UI básica | 1. Esqueleto de proyecto + dependencias (Gradle, Hilt, Room, Compose)<br>2. Base de datos local y entidades (Room)<br>3. Capa Repository (interfaces + implementaciones)<br>4. UI básica con Flow (Dashboard, SymptomLog)<br>5. Pantallas Lab y Medication<br>6. Componentes UI reutilizables (ui/components/)<br>7. Exportaciones CSV | 1 día (≈ 8 hrs) |
| **Sprint 2** | Lógica de recomendación | 1. Engine de recomendaciones (ATA/Endocrine Society 2020‑2022)<br>2. Exportaciones detalladas (PDF, ZIP)<br>3. Pruebas unitarias + CI | 3 días ≈ 24 hrs |
| **Sprint 3** | Experiencia + herramientas | 1. Dashboard de tendencias (gráficos)<br>2. Plan de alimentación personalizado<br>3. Programa de ejercicio adaptado<br>4. Notificaciones push personalizadas<br>5. Módulo de diario/personalidad<br>6. Chat‑bot auto‑diagnóstico con IA (OpenAI API)<br>7. Backup ZIP cifrado<br>8. Soporte multi‑idioma<br>9. Otros temas pendientes | 3 días ≈ 24 hrs |

### Integración IA (OpenAI)
- Se añadirá un módulo de asistente de IA para responder preguntas del paciente sobre síntomas, interpretación de resultados de laboratorio, recomendaciones de suplementación y estilo de vida.
- Se usará la API de OpenAI con clave proporcionada por el usuario.
- Todas las respuestas incluirán descargo de responsabilidad: *"No soy médico, consulte a su endocrinólogo"*.
