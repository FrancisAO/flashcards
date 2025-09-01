# Instructions
## Controller
Immer wenn du Spring Web Controller-Klassen mit `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping` oder `@RequestMapping` erstellst oder bearbeitest, achte unbedingt auf Folgendes:

1. **`@PathVariable`**: Gib den Variablennamen immer explizit an, auch wenn der Methodenparameter den gleichen Namen hat.

   * Beispiel:

     ```java
     @GetMapping("/api/v1/projects/{id}")
     public ProjectDto getProject(@PathVariable("id") String projectId) { ... }
     ```
   * **Nicht erlaubt**: `@PathVariable String id` ohne Namen.
2. **`@RequestParam`**: Benenne den Parameter ebenfalls immer explizit.

   * Beispiel:

     ```java
     @GetMapping("/api/v1/search")
     public List<ProjectDto> search(@RequestParam("q") String query) { ... }
     ```
   * **Nicht erlaubt**: `@RequestParam String q` ohne Namen.
3. Diese Regel gilt **für alle neuen und bestehenden Mapping-Methoden**, auch bei Refactorings.

Ziel ist es, Fehler wie `IllegalArgumentException: Name for argument not specified` zu vermeiden, die entstehen, wenn das Compiler-Flag `-parameters` nicht aktiv ist.

**Kurzregel:**
*Jedes `@PathVariable` und jedes `@RequestParam` muss immer ein benanntes Argument haben.
