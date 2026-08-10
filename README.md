# 🧮 Smart Calculator (Kotlin)

A command-line **Smart Calculator** built with Kotlin as part of the **JetBrains Academy / Hyperskill** Kotlin Developer track.

This project parses, validates, and evaluates complex mathematical expressions with support for arbitrary variables, negative numbers, nested parentheses, and operator precedence using core Computer Science algorithms.

---

## ✨ Features

- **Expression Evaluation:** Handles arithmetic operations (`+`, `-`, `*`, `/`, `^`) respecting standard operator precedence.
- **Variable Storage:** Allows assignment and evaluation of dynamic variables (e.g., `a = 5`, `b = a + 2`).
- **Infix to Postfix Conversion:** Implements the **Shunting-Yard algorithm** to convert infix expressions into Reverse Polish Notation (RPN).
- **Postfix Evaluator:** Uses a **Stack** (`ArrayDeque`) data structure to compute results reliably in $O(n)$ time.
- **Robust Parsing & Tokenization:** Custom Regex engine capable of distinguishing binary operators (subtraction) from unary operators (negative numbers).
- **Error Handling:** Validates syntax errors, unbalanced parentheses, invalid variable names, and unknown identifiers.

---

## 🛠️ Technical Stack & Concepts

- **Language:** Kotlin
- **Data Structures:** Stack (`ArrayDeque`), Hash Map (`MutableMap`)
- **Algorithms:** Shunting-Yard Algorithm (Infix to Postfix), Postfix Evaluation
- **Text Processing:** Regular Expressions (Regex), Tokenization with `findAll` and lookbehinds
- **Paradigm:** Object-Oriented & Functional Programming (immutability with `data class`)

---

## 🚀 How to Run

1. Clone this repository:
   ```bash
   git clone [https://github.com/tu-usuario/smart-calculator.git](https://github.com/tu-usuario/smart-calculator.git)
