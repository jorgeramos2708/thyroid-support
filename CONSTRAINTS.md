# CONSTRAINTS.md – Reglas de Calidad, Privacidad y Seguridad

## Privacidad y Seguridad
1. **Sin nube**: todos los datos se almacenan localmente en el dispositivo. No se permite sincronización con servicios externos excepto exportación manual a CSV/PDF/ZIP.
2. **Cifrado en reposo**: la base de datos Room se cifra con SQLCipher utilizando una clave almacenada en Android Keystore.
3. **Preferencias cifradas**: DataStore / EncryptedPreferences para configuración de usuario.
4. **Sin tracking**: no se incluye analítica ni publicidad.
5. **API Key de OpenAI**: se almacena cifrada en EncryptedSharedPreferences (Jetpack Security). Nunca en código fuente ni en Room.
6. **Protección de datos de salud (PHI)**: está **prohibido** enviar datos identificables del paciente (nombre, fecha de nacimiento, género, ID interno) a OpenAI o a cualquier servicio externo. Solo se pueden enviar datos anonimizados de síntomas o resultados de laboratorio con fines de consulta informativa.
7. **Descargo de responsabilidad IA**: toda respuesta generada por IA incluye aviso: *"No soy médico, consulte a su endocrinólogo"*.

## Arquitectura
8. **Clean Architecture + MVVM + Hilt**: separación estricta entre dominio, datos y presentación.
9. **Capa Repository**: ViewModels no hablan directamente con DAOs; usan repositorios.
10. **Room para persistencia**: solo se usa SQLite local (Room).
11. **Jetpack Compose para UI**: interfaz declarativa y accesible (WCAG AA).

## Calidad de código
12. **Tests obligatorios**: cada módulo de dominio y data debe tener pruebas unitarias.
13. **Kotlin coroutines + Flow**: operaciones asíncronas sin bloquear el hilo principal.
14. **Commit contextual**: usar `contextual-commit` con cuerpo que explique el porqué.
