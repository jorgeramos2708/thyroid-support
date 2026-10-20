# Thyroid‑Support

A local‑only Android application to track thyroid disease (hyper‑/hypo‑thyroidism) symptoms, lab values, medications, supplements, diet and exercise.  All data is stored encrypted on the device; no cloud sync.

## Architecture
- MVVM with Jetpack Compose, Hilt and Room (EncryptedRoom via SQLCipher)
- Domain‑first: `domain/` (entities), `data/` (DAOs, Repository), `presentation/` (ViewModel + UI)
- Recommendation engine in Kotlin, based on ATA/Endocrine Society guidelines (2020‑2022).
- CI via GitHub Actions: build, lint, unit tests, `lint -p`.

## Features
1. Log symptoms (type, severity, notes).
2. Log lab results (TSH, FT4, FT3, antibodies, etc.).
3. Medications/Supplements tracking.
4. Diet & exercise logs.
5. Recommendations (medication dose, supplements, diet, exercise).
6. Export CSV / PDF.
7. Push notifications for medication and labs.
8. Secure data (EncryptedRoom, encrypted DataStore).

## Get Started
```bash
git clone https://github.com/jorgeramos2708/thyroid-support
cd thyroid-support
# set up Android project (depends on Android Studio)
```
