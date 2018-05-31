# Spark application

Esta aplicación representa un simple ABM de items. Se utilizó una arquitectura
MVC implementada con Java y Spark. mientras que para la persistencia de la 
información se utilizó Elastic Search.

# Routes

| Recurso  | Descripción | Método |
| ------------- | ------------- | ------------- |
| /items  | Permite obtener listado de items  | GET  |
| /item/{itemId}  | Permite obtener un item en particular  | GET  |
| /create  | Permite la creación de un nuevo item  | POST  |
| /update/{itemId}  | Permite actualizar un item existente | PUT  |
| /delete/{itemId}  | Permite eliminar un item existente  | DELETE  |


# Estructura de un item

Un item esta compuesto por los siguientes campos:

- itemId : string : obligatorio
- title : string : obligatorio
- description : string : obligatorio
- categoryId : string : obligatorio
- price : float : obligatorio
- currencyId : string
- availableQuantity : int
- buyingMode : string
- listingTypeId : string
- condition : string
- videoId : string
- warranty : string
- pictures : string