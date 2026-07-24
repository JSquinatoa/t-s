# Cheat Sheet: Relaciones en Java (Hibernate / Panache)

## 1. Varios a Uno (@ManyToOne)
**Ejemplo Clásico:** Muchas Tareas pertenecen a un solo Usuario (o a una sola Categoría).

En la clase `Tarea` (El lado "Muchos"):
```java
@Entity
public class Tarea extends PanacheEntityBase {
    
    // ... tus otros campos (id, titulo, etc)

    // LAZY = No carga los datos completos de la categoría hasta que tú se los pidas explícitamente (buena práctica).
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id") // Este será el nombre de la columna en la base de datos
    public Categoria categoria;
    
}
```
*💡 Tip de Examen: El lado `@ManyToOne` es siempre el "dueño" de la relación. Es la tabla que guarda físicamente el número de ID (llave foránea).*

---

## 2. Uno a Varios (@OneToMany)
**Ejemplo Clásico:** Una Categoría tiene Muchas Tareas.

En la clase `Categoria` (El lado "Uno"):
```java
@Entity
public class Categoria extends PanacheEntityBase {
    
    // ... tus otros campos

    // mappedBy = "categoria" significa que el dueño de esta relación es la variable "categoria" de la otra clase.
    // cascade = ALL significa que si borras esta categoría, se borrarán todas sus tareas asociadas.
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    public List<Tarea> tareas;
    
}
```

---

## 🔥 Reglas de Oro que te salvarán la vida:

1. **¿Dónde va el `@JoinColumn`?**
   **SIEMPRE** ponlo junto a `@ManyToOne`. 

2. **¿Dónde va el `mappedBy="..."`?**
   **SIEMPRE** ponlo junto a `@OneToMany` (en la Lista). Y recuerda que el texto de adentro debe ser el nombre exacto de la variable que declaraste en la otra clase.

3. **¿Es obligatorio hacer los dos lados (Bidireccional)?**
   **NO.** A menos que el profesor te lo pida. Si en el examen solo necesitas saber de quién es una Tarea, basta con poner el `@ManyToOne` en la clase Tarea y listo. Te ahorras crear la Lista en la otra clase.
