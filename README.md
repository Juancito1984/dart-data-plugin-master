# Dart Data Section NyK

[![Language: English](https://img.shields.io/badge/Language-English-blue.svg)](README_EN.md)
[![Idioma: Español](https://img.shields.io/badge/Idioma-Español-red.svg)](README_ES.md)
[![Architecture](https://img.shields.io/badge/Technical-Architecture%20Guide-green.svg)](docs/ARCHITECTURE.md)
[![Platform](https://img.shields.io/badge/IntelliJ%20Platform-241%2B%20%7C%20262%2B-orange.svg)](https://plugins.jetbrains.com/)
[![Gradle](https://img.shields.io/badge/Gradle-9.9.0-02303A.svg)](https://gradle.org/)
[![Java](https://img.shields.io/badge/JVM-17%2B-ED8B00.svg)](https://www.oracle.com/java/)

🌐 **Documentación / Documentation:**
- [English Documentation](README_EN.md)
- [Documentación en Español](README_ES.md)
- [Guía Técnica y Arquitectura del Proyecto](docs/ARCHITECTURE.md)

---

## 📌 Resumen Ejecutivo / Executive Summary

### 🇬🇧 English
**Dart Data Section NyK** is an advanced code generation plugin for Android Studio and IntelliJ Platform IDEs. Maintained and enhanced by **Ing. Juan Pablo Gosalvez**, it automatically generates essential data class utility methods for Dart, including:
- **Named Argument Constructors** (with `required`, `const`, and null-safety).
- **`copyWith`** (with immutable instance reuse optimization).
- **Serialization (`toMap` & `fromMap`)** with full support for Flutter Firestore `Timestamp`, `DateTime`, and custom key mapping.
- **Value Equality (`==`) & `hashCode`**.
- **Formatted `toString()`** using string interpolation.

Fully compatible with **Android Studio 2024.1 (Koala) through Build 262+ (JBR 25, Ladybug, Meerkat and future releases)**, built on **Gradle 9.9.0**, **Kotlin 2.1.20**, and **Java 17**.

👉 [Read the complete English Documentation](README_EN.md)

### 🇪🇸 Español
**Dart Data Section NyK** es un plugin avanzado de generación de código para Android Studio e IntelliJ Platform. Mantenido y optimizado por el **Ing. Juan Pablo Gosalvez**, genera automáticamente métodos esenciales de manipulación de datos en clases de Dart:
- **Constructores con argumentos nombrados** (con soporte para `required`, `const` y null-safety).
- **`copyWith`** (con optimización de reutilización de instancias inmutables).
- **Serialización (`toMap` y `fromMap`)** con soporte nativo para `Timestamp` de Firestore, `DateTime` y mapeo de claves.
- **Igualdad de valor (`==`) y `hashCode`**.
- **`toString()` formateado** con interpolación de cadenas.

Totalmente compatible con **Android Studio 2024.1 (Koala) hasta la Build 262+ (JBR 25, Ladybug, Meerkat y versiones futuras)**, compilado con **Gradle 9.9.0**, **Kotlin 2.1.20** y **Java 17**.

👉 [Leer la documentación completa en Español](README_ES.md)

---

## 🚀 Vista Previa de Uso / Usage Preview

![Uso](img/dart-data-usage.gif)

---

## ⚙️ Especificaciones Técnicas / Technical Specifications

| Componente / Component | Versión / Requisito |
|---|---|
| **IntelliJ Platform Plugin SDK** | `2.20.0` |
| **Android Studio Target** | `2024.1.1.13` (Build 241+) con soporte abierto hasta Build 262+ |
| **Kotlin Gradle Plugin** | `2.1.20` |
| **Gradle Wrapper** | `9.9.0` |
| **Java Compatibility** | Java 17 (`sourceCompatibility` & `targetCompatibility = 17`) |
| **Threading Model** | `ActionUpdateThread.BGT` (Background Threading estricto) |
| **Write Model** | `WriteCommandAction.runWriteCommandAction` |

---

## 📦 Compilación e Instalación / Build & Installation

### Compilación desde código fuente / Build from source
```bash
# Ejecutar pruebas unitarias
./gradlew test

# Generar archivo empaquetado del plugin (.zip)
./gradlew clean buildPlugin
```
El artefacto resultante se ubicará en:
```
build/distributions/dart-data-plugin-1.0.1.zip
```

### Instalación en Android Studio / Installation in Android Studio
1. Abre **Android Studio** o tu IDE IntelliJ Platform preferido.
2. Ve a **Settings / Preferences** (`Ctrl + Alt + S`).
3. Selecciona **Plugins** > Haz clic en el ícono de engranaje ⚙️ > **Install Plugin from Disk...**.
4. Selecciona el archivo `.zip` generado en `build/distributions/dart-data-plugin-1.0.1.zip`.
5. Reinicia el IDE cuando se solicite.
