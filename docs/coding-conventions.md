## Style

### Import
- Wildcard imports (`import x.y.*`) should not be used.
- Imports should be sorted (use `Ctrl+Alt+o` in IntelliJ IDEA)

## Coding
### Minimal Accessibility
Always use the access modifiers with minimal accessibility for classes/methods/fields, e.g., if an inner-class/method/field is not used by any other classes, use `private` modifier.

### Use `final` Field
When possible, declare `final` fields.

### Return Unmodifiable Collection
```
class Graph {
    private Set<Node> nodes;

    public Set<Node> getNodes() {
        // return nodes;
        return Collections.unmodifiableSet(nodes);
    }
}
```

### Output via Logger
Always use logger to output messages.

### Annotation (@Override, @Nullable, @Nonnull, ...)
Always add `@Override` annotation for overridden methods.

For the methods that may return `null`, annotate them with `@Nullable`.

For the methods that require non-`null` arguments, add `@Nonnull` annotation to the specific parameters, For example, `void setX(@Nonnull x)`.

### Use Tai-e Util
- Use `Sets`/`Maps` to create Sets/Maps.
  When creating Set/Map, use proper `Sets.newSet`/`Maps.newMap()` factory methods instead of `new HashSet/Map<>()`.

- Tai-e provides some data structures (in package `pascal.tai.util.collection`) that are commonly-used in static analysis but not included in JDK, e.g., `MultiMap` and `TwoKeyMap`. You could use them to make life easier.

- Use `Hashes.hash()` to compute hash value of multiple objects. If the arguments may be `null`, use `Hashes.safeHash()`.

- Obtain string constants from string providers.
  When using JDK class names, method names, or signatures, refer to corresponding fields of `ClassNames`, `MethodNames`, or `Signatures` (these classes are annotated by `@StringProvider`).
