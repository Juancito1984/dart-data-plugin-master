# Dart Data Class Plugin

> **Note**: This is a customized fork ("Dart Data Section NyK") of the original [Dart Data Class Plugin](https://github.com/andrasferenczi/dart-data-plugin) created by **Andras Ferenczi**. Thank you, Andras, for your initiative in creating this wonderful tool! This fork includes modern Dart features such as enhanced Null-Safety, String Interpolation, and robust Timestamp/DateTime support.

![Usage](img/dart-data-usage.gif)

This plugin is created for those who would like to have extra data manipulation methods in their data classes without having to write boilerplate code.

![ScreenShot](img/generate-menu.png)

This plugin can generate the following code:
- Named Argument Constructor
- `copyWith`: copies the instance and overrides the given parameters
- `toMap`: converts your class into a map of `Map<String, dynamic>`
- `fromMap`: constructs your class from a `Map<String, dynamic>` using the named argument constructor
- all of the above with `toString`, `equals` (`==` operator), `hashcode`

## Example

Let's say you have a class called `LibroModel` with the properties:

```dart
class LibroModel {
  final String id;
  final String titulo;
  final int? anio;
  final String autorId;
  final DateTime create;
}
```

Using all generators of this plugin and selecting all properties, this class will look like the following. 

*(Note that some features of this generation can be customized, see details below, and it includes robust parsing for dates like Firestore's Timestamp!)*


```dart
class LibroModel {
  final String id;
  final String titulo;
  final int? anio;
  final String autorId;
  final DateTime create;

//<editor-fold desc="Data Methods">
  const new({
    required this.id,
    required this.titulo,
    this.anio,
    required this.autorId,
    required this.create,
  });

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      (other is LibroModel &&
          runtimeType == other.runtimeType &&
          id == other.id &&
          titulo == other.titulo &&
          anio == other.anio &&
          autorId == other.autorId &&
          create == other.create);

  @override
  int get hashCode =>
      id.hashCode ^
      titulo.hashCode ^
      anio.hashCode ^
      autorId.hashCode ^
      create.hashCode;

  @override
  String toString() {
    return 'LibroModel{id: $id, titulo: $titulo, anio: $anio, autorId: $autorId, create: $create}';
  }

  LibroModel copyWith({
    String? id,
    String? titulo,
    int? anio,
    String? autorId,
    DateTime? create,
  }) {
    return LibroModel(
      id: id ?? this.id,
      titulo: titulo ?? this.titulo,
      anio: anio ?? this.anio,
      autorId: autorId ?? this.autorId,
      create: create ?? this.create,
      income: income ?? this.income,
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'id': this.id,
      '_firstName': this._firstName,
      '_lastName': this._lastName,
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'titulo': titulo,
      'anio': anio,
      'autorId': autorId,
      'create': create,
    };
  }

  factory LibroModel.fromMap(Map<String, dynamic> map) {
    return LibroModel(
      id: map['id'] as String? ?? '',
      titulo: map['titulo'] as String? ?? '',
      anio: (map['anio'] as num?)?.toInt(),
      autorId: map['autorId'] as String? ?? '',
      create: map['create'] != null
          ? (map['create'] is DateTime
                ? map['create'] as DateTime
                : (map['create'].runtimeType.toString() == 'Timestamp'
                      ? (map['create'] as dynamic).toDate() as DateTime
                      : DateTime.parse(map['create'].toString())))
          : DateTime.now(),
    );
  }

//</editor-fold>
}
```

## Settings

![ScreenShot](img/settings-menu.png)

You can find additional settings under `Settings` > `Editor` > `Dart Data Class Plugin` where you have the following customization options:

- set the name of the copy method
- use `@required` annotation
- use `new` keyword when instantiation
- use the `const` keyword for the constructor generation - all fields in the class have to be final
- copy function can be specified to return the same instance. This Option has the same requirement as the const keyword. Useful when using Dart in a Redux architecture.
- key mapper for `toMap` and `fromMap` - use your own logic to transform the keys to their original values - useful when database result returns prefixed or uppercased result


#### Under the hood

This project is built using Kotlin and makes use of IntelliJ's PSI elements for extracting the structure of the Dart file of your selection.

### Known issues

- If the class ends with a single-line comment, code is generated from an invalid location
