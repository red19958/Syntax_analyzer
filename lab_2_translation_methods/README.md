# Лабораторная работа №2. Ручное построение нисходящих синтаксических анализаторов

### Вариант 9. Описание заголовка функции в Kotlin

Заголовок функции в Kotlin. Заголовок начинается ключевым словом “fun”, далее идет имя функции, скобка, несколько
описаний аргументов через запятую, затем может идти двоеточие и имя возвращаемого
типа.
Используйте один терминал для всех имен переменных. Используйте
один терминал для ключевых слов fun и т. п. (не несколько ‘f’, ‘u’, ‘n’).
Пример:

```Kotlin 
fun printSum(a: Int, b: Int): Unit
```

## Грамматика

| Нетерминал                           | Описание                                                 |
|--------------------------------------|----------------------------------------------------------|
| `START -> fun FUN_NAME`              | Описание заголовка функции в Kotlin.                     |
| `FUN_NAME -> word (ARGS)RETURN_TYPE` | Описание списка аргументов в скобках и возвращаемый тип. |
| `ARGS -> REST_ARGS`                  | Список аргументов через запятую.                         |
| `ARGS -> ε`                          | В случае если функция не принимает аргументов.           |
| `REST_ARGS -> word: wordCOMMA`       | Продолжение списка аргументов.                           |
| `COMMA -> , REST_ARGS`               | Запятая.                                                 |
| `COMMA -> ε`                         | Конец списка аргументов.                                 |
| `RETURN_TYPE -> : word`              | Возвращаемый тип.                                        |
| `RETURN_TYPE -> ε`                   | В случае если функция не имеет возвращаемого типа.       |

## First and Follow

| Нетерминал    | FIRST           | FOLLOW          |
|---------------|-----------------|-----------------|
| `START`       | `fun`           | `word`          |
| `FUN_NAME`    | `word`          | `(`             |
| `ARGS`        | `word: word, ε` | `\,, )`         |
| `REST_ARGS`   | `word: word`    | `\,,)`          |
| `COMMA`       | `\,, ε`         | `word: word, )` |
| `RETURN_TYPE` | `: type, ε`     | `$`             |

## Терминалы

| Терминал | Токен          |
|----------|----------------|
| `fun`    | FUN            |
| `word`   | WORD           | 
| `(`      | OPENED_BRACKET |
| `)`      | CLOSED_BRACKET |
| `:`      | COLON          | 
| `,`      | COMMA          |
| `$`      | END            |

## Tests

| Тест                                      | Описание                                                |
|-------------------------------------------|---------------------------------------------------------|
| `fun @()`                                 | Simple тест                                             |
| `fun @(): #`                              | Тест на правило RETURN_TYPE -> : word                   |
| `fun @(@: #)`                             | Тест на правило REST_ARGS -> word: wordCOMMA            |
| `fun @(@: #, @: #)`                       | Тест на правило COMMA -> , REST_ARGS                    |
| `fun @(@: #, @: #): #`                    | Big тест                                                |
| `fun @(@:#,@:#):#`                        | Тест без ненужных пробелов                              |
| `fun @ ( @ : # , @ : # ) : #`             | Тест на simple spaces между терминалами                 |
| `fun\r@\r(\r@\r:\r#\r,\r@\r:\r#\r)\r:\r#` | Тест на символ возврата каретки                         |
| `fun\t@\t(\t@\t:\t#\t,\t@\t:\t#\t)\t:\t#` | Тест на символ табуляции                                |
| `fun\n@\n(\n@\n:\n#\n,\n@\n:\n#\n)\n:\n#` | Тест на символ новой строки                             |
| `fun@()`                                  | Тест на отделяемость ключевого слова fun                |
| `fun ()`                                  | Тест на существование имени функции                     |
| `fun @(`                                  | Тест на существование закрывающей скобки                |
| `fun @)`                                  | Тест на существование открывающей скобки                |
| `fun @() #`                               | Тест на существование : до возвращаемого типа           |
| `fun @(@ #)`                              | Тест на существование : между типом и аргументом        |
| `fun @():`                                | Тест на существование возвращаемого типа после :        |
| `fun @(: #)`                              | Тест на существование имени аргумента                   |
| `fun @(@: )`                              | Тест на существование типа у аргумента                  |
| `fun @(@: # @: #)`                        | Тест на существование , между перечислениями аргументов |