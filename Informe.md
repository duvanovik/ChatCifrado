# Informe del Proyecto de Chat Cifrado

## Introducción

Este informe relata mi experiencia desarrollando un sistema de chat cifrado en Java. Mi objetivo era establecer una comunicación segura entre dos instancias del programa a través de una red. Para lograrlo, implementé la negociación de claves con el algoritmo Diffie-Hellman y utilicé AES para el cifrado de la conversación.

## Desarrollo

El proceso de creación se dividió en las siguientes fases:

- **Establecimiento de Conexión de Red**: Configuré sockets en Java para permitir la comunicación entre un servidor y un cliente.
- **Negociación de Claves con Diffie-Hellman**: Generé pares de claves públicas y privadas y realicé el intercambio de las claves públicas para establecer una clave compartida.
- **Cifrado y Descifrado con AES**: Procesé la clave compartida a través de SHA-256 para adaptarla a los 256 bits necesarios y la utilicé para el cifrado y descifrado AES de los mensajes.

### Código y Colaboración

Coloqué el código fuente en un repositorio de GitHub, facilitando la colaboración y el seguimiento de los cambios.

## Dificultades

Durante el desarrollo, enfrenté varios desafíos:

- **Errores de Criptografía**: Al principio, hubo problemas al intentar utilizar directamente la clave compartida Diffie-Hellman para el cifrado AES, los cuales resolví aplicando un hash SHA-256 para obtener la longitud correcta de la clave.
- **Manejo de Errores**: Entendí la importancia de un manejo de errores robusto, particularmente en las operaciones de red y las de cifrado/descifrado.
- **Pruebas de Funcionalidad**: Realizar pruebas para verificar la correcta implementación de Diffie-Hellman y el cifrado AES requirió una cuidadosa depuración y validación.

## Conclusiones

El proyecto fue exitoso y se cumplieron los objetivos propuestos:

- **Funcionalidad**: Conseguí implementar un chat cifrado con una clave de cifrado segura acordada a través de Diffie-Hellman.
- **Aprendizaje**: Este proyecto fue una valiosa oportunidad de aprendizaje para comprender mejor los principios de la criptografía aplicada.
- **Seguridad**: Aunque el chat es seguro dentro del alcance del proyecto, para un uso más amplio serían necesarias auditorías de seguridad adicionales.

## Contribuciones

Si deseas contribuir a este proyecto, por favor consulta las instrucciones en `CONTRIBUTING.md`.

---

