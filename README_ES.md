# Dart Data Section NyK

> **Nota**: Este es un fork personalizado ("Dart Data Section NyK") del [Dart Data Class Plugin](https://github.com/andrasferenczi/dart-data-plugin) original creado por **Andras Ferenczi**. ¡Muchas gracias a Andras por su iniciativa al crear esta maravillosa herramienta! Esta versión personalizada, mantenida por el **Ing. Juan Pablo Gosalvez**, incluye características modernas de Dart como Null-Safety robusto, interpolación de cadenas, manejo avanzado de `Timestamp` (Firestore) y compatibilidad total con las plataformas más recientes de Android Studio e IntelliJ.

![Uso](img/dart-data-usage.gif)

Este plugin está diseñado para desarrolladores que desean generar métodos adicionales de manipulación de datos en sus clases de Dart sin tener que escribir código repetitivo (boilerplate).

![Captura de pantalla](img/generate-menu.png)

## Características Principales

Este plugin genera de forma automática:
- **Constructor con argumentos nombrados (*Named Argument Constructor*):** Con soporte para `required`, valores por defecto, constructor `const` y Null-Safety.
- **`copyWith`:** Copia la instancia sobrescribiendo únicamente los parámetros seleccionados, con soporte de inmutabilidad y reutilización opcional de la misma instancia.
- **`toMap`:** Serializa tu clase a una estructura de tipo `Map<String, dynamic>`.
- **`fromMap`:** Reconstruye la clase desde un `Map<String, dynamic>` utilizando el constructor con argumentos nombrados y validación segura de tipos (incluyendo `DateTime` y `Timestamp`).
- **`toString`:** Representación legible en texto con interpolación moderna de cadenas Dart.
- **`equals` (`operator ==`) y `hashCode`:** Comparación por valor de propiedades.
- **Sección consolidada "Dart Data Section NyK":** Agrupa todas las funciones anteriores dentro de un bloque colapsable `<editor-fold desc="Data Methods">`.

---

## Ejemplo Completo

Dada una clase Dart con propiedades con diferentes tipos y nulabilidad:

```dart
class Person {
  final String id;
  final String nombre;
  final int? edad;
  final String? direccion;
  final DateTime fechaNacimiento;
}
```

Al invocar la acción **Dart Data Section NyK** (`Alt + Insert` o menú contextual `Generate...` > `Dart Data Section NyK`) y seleccionar todas las propiedades, se genera el siguiente código limpio y estructurado:

```dart
class Person {
  final String id;
  final String nombre;
  final int? edad;
  final String? direccion;
  final DateTime fechaNacimiento;

//<editor-fold desc="Data Methods">
  const Person({
    required this.id,
    required this.nombre,
    this.edad,
    this.direccion,
    required this.fechaNacimiento,
  });

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      (other is Person &&
          runtimeType == other.runtimeType &&
          id == other.id &&
          nombre == other.nombre &&
          edad == other.edad &&
          direccion == other.direccion &&
          fechaNacimiento == other.fechaNacimiento);

  @override
  int get hashCode =>
      id.hashCode ^
      nombre.hashCode ^
      edad.hashCode ^
      direccion.hashCode ^
      fechaNacimiento.hashCode;

  @override
  String toString() {
    return 'Person{id: $id, nombre: $nombre, edad: $edad, direccion: $direccion, fechaNacimiento: $fechaNacimiento}';
  }

  Person copyWith({
    String? id,
    String? nombre,
    int? edad,
    String? direccion,
    DateTime? fechaNacimiento,
  }) {
    return Person(
      id: id ?? this.id,
      nombre: nombre ?? this.nombre,
      edad: edad ?? this.edad,
      direccion: direccion ?? this.direccion,
      fechaNacimiento: fechaNacimiento ?? this.fechaNacimiento,
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'nombre': nombre,
      'edad': edad,
      'direccion': direccion,
      'fechaNacimiento': fechaNacimiento,
    };
  }

  factory Person.fromMap(Map<String, dynamic> map) {
    return Person(
      id: map['id'] as String? ?? '',
      nombre: map['nombre'] as String? ?? '',
      edad: (map['edad'] as num?)?.toInt(),
      direccion: map['direccion'] as String?,
      fechaNacimiento: map['fechaNacimiento'] != null
          ? (map['fechaNacimiento'] is DateTime
                ? map['fechaNacimiento'] as DateTime
                : (map['fechaNacimiento'].runtimeType.toString() == 'Timestamp'
                      ? (map['fechaNacimiento'] as dynamic).toDate() as DateTime
                      : DateTime.parse(map['fechaNacimiento'].toString())))
          : DateTime.now(),
    );
  }

//</editor-fold>
}
```

---

## Configuración

![Configuración](img/settings-menu.png)

El plugin ofrece configuraciones avanzadas accesibles en **Settings / Preferences** > **Editor** > **Dart Data Class Plugin**:

- **Nombre del método de copia:** Personaliza el identificador (por defecto `copyWith`).
- **Uso de `@required`:** Habilitar/deshabilitar la anotación `@required` (en proyectos sin Null-Safety o con versiones legacy).
- **Palabra clave `new`:** Habilitar o desactivar el prefijo `new` en instanciaciones.
- **Palabra clave `const`:** Generar constructores `const` automáticamente cuando todos los miembros de la clase sean `final`.
- **Optimización de copia:** Si todos los campos son inmutables y no se pasa ningún argumento a `copyWith`, reutilizar la instancia existente.
- **Mapeador de claves para `toMap` y `fromMap`:** Permite aplicar lógica personalizada de mapeo de nombres de propiedades.

---

## Arquitectura y Compatibilidad Técnica

- **Entorno de ejecución:** Compatible con **Android Studio 2024.1 (Koala)** hasta **Build 262+** (y versiones futuras con JBR OpenJDK 25 en Windows).
- **Gradle:** Construido sobre **Gradle 9.9.0** con **Kotlin 2.1.20**.
- **Compatibilidad JVM:** Código objetivo compilado para **Java 17** (`jvmTarget = 17`).
- **Threading Seguro:** Acciones auditadas e integradas bajo el hilo en segundo plano de la plataforma (`ActionUpdateThread.BGT`) para garantizar que la interfaz de usuario no sufra bloqueos (sin congelamiento del EDT).
- **Acceso a Escritura Seguro:** Uso de `WriteCommandAction.runWriteCommandAction` para asegurar la transaccionalidad y soporte completo de deshacer/rehacer (`Undo/Redo`).

---

## Compilación y Pruebas

Para compilar el plugin y ejecutar la suite de pruebas unitarias:

```bash
# Ejecutar pruebas unitarias
./gradlew test

# Compilar y empaquetar el plugin limpio
./gradlew clean buildPlugin
```

El artefacto final empaquetado se encontrará en:
```
build/distributions/dart-data-plugin-1.0.1.zip
```

---

## 📖 Arquitectura y Guía para Desarrolladores

Para conocer los detalles internos sobre el ciclo de vida de las acciones, el árbol PSI de Dart, los hilos de ejecución en segundo plano (`ActionUpdateThread.BGT`) y las plantillas de generación de código, consulta la [Guía Técnica de Arquitectura](docs/ARCHITECTURE.md).

