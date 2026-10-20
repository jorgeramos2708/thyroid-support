# Tareas Sprint 1

| ID | Tarea | Estado | Componente |
|----|-------|--------|------------|
| T1 | Inicializar repo y crear build.gradle.kts (raíz + app) | done | Gradle |
| T2 | Crear settings.gradle.kts | done | Gradle |
| T3 | Crear AndroidManifest.xml con permisos y ThyroidApplication | done | Manifest |
| T4 | Crear MainActivity con Compose + ThyroidApp | done | UI |
| T5 | Crear entidades de dominio (Patient, Symptom, Lab, Medication, Supplement) | done | Domain |
| T6 | Crear DAOs (PatientDao, SymptomDao, LabDao, MedicationDao, SupplementDao) | done | Data |
| T7 | Crear AppDatabase con Room | done | Data |
| T8 | Crear módulos Hilt (DatabaseModule, AppModule, RepositoryModule) | done | DI |
| T9 | Crear Repository interfaces en domain/repository/ | done | Domain |
| T10 | Crear Repository implementaciones en data/repository/ | done | Data |
| T11 | Crear ViewModels (DashboardViewModel, SymptomViewModel, LabViewModel, MedicationViewModel) | done | Presentation |
| T12 | Crear pantallas Compose (DashboardScreen, SymptomLogScreen, LabScreen, MedicationScreen) | done | UI |
| T13 | Crear tema Material3 y recursos (colors, strings, themes) | done | UI |
| T14 | Crear navegación con Compose Navigation | done | Navigation |
| T15 | Crear componentes UI reutilizables (ui/components/) | done | UI |
| T16 | Compilar proyecto y verificar `assembleDebug` | done | CI |
| T17 | Crear commit inicial en rama `feature/setup` | done | Git |

# Tareas Sprint 2

| ID | Tarea | Estado | Componente |
|----|-------|--------|------------|
| T18 | Implementar Engine de recomendaciones (ATA/Endocrine Society 2020‑2022) | done | Domain |
| T19 | Crear entidades Recommendation, LabResultAnalysis | done | Domain |
| T20 | Crear RecommendationRepository + implementación | done | Data |
| T21 | Crear RecommendationViewModel + RecommendationScreen | done | UI |
| T22 | Agregar navegación a RecommendationScreen | done | Navigation |
| T23 | Implementar ExportRepository + ExportRepositoryImpl (CSV/ZIP) | done | Data |
| T24 | Crear ExportViewModel + ExportScreen | done | UI |
| T25 | Agregar navegación a ExportScreen | done | Navigation |
| T26 | Pruebas unitarias para RecommendationRepositoryImpl | done | Testing |
| T27 | Configurar GitHub Actions CI (build + tests) | done | CI |

# Tareas Sprint 3 (pendiente)

| ID | Tarea | Estado | Componente |
|----|-------|--------|------------|
| T28 | Dashboard de tendencias (gráficos) | pending | UI |
| T29 | Plan de alimentación personalizado | pending | Domain |
| T30 | Programa de ejercicio adaptado | pending | Domain |
| T31 | Notificaciones push personalizadas | pending | UI |
| T32 | Módulo de diario/personalidad | pending | Domain |
| T33 | Chat‑bot auto‑diagnóstico con IA OpenAI | pending | IA |
| T34 | Backup ZIP cifrado | pending | Data |
| T35 | Soporte multi‑idioma | pending | UI |
