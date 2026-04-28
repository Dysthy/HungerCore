# HungerCore

Un plugin de Minecraft para gestionar eventos de Hunger Games (Hunger Craft 3).

## Características

- **Gestión de Tributos**: Sistema para manejar tributos y sus estadísticas
- **Sistema de Recompensas**: Recompensas por cabeza de tributos
- **Gestión de Cofres**: Sistema de cofres con contenido aleatorio
- **Puntos de Spawn**: Gestión de puntos de aparición para tributos
- **Sistema de Temporizadores**: Temporizadores configurables para eventos
- **Integración con PlaceholderAPI**: Placeholders para mostrar información en el chat
- **Sistema de Muerte**: Los jugadores muertos se quedan en modo espectador en el lugar donde murieron
- **Sistema de Equipos**: Integración con Lead-API para gestionar equipos de tributos

## Comandos

- `/hc spawnpoints` - Gestionar puntos de spawn
- `/hc chests` - Gestionar cofres de recursos
- `/hc tributes` - Gestionar tributos
- `/hc lobby` - Configurar ubicación del lobby
- `/hc timer` - Configurar temporizadores
- `/hc bounty` - Gestionar recompensas por cabeza
- `/hc teams` - Gestionar equipos de tributos (nuevo)
- `/hc revive <jugador>` - Revivir un jugador muerto (solo ops)
- `/hc reviveall` - Revivir a todos los jugadores muertos (solo ops)
- `/hc resetkills` - Resetear todas las kills (solo ops)
- `/hc resetplayerkills <jugador>` - Resetear kills de un jugador específico (solo ops)
- `/hc cleardeaths` - Limpiar todas las ubicaciones de muerte (solo ops)
- `/hc updatealive` - Actualizar contador de jugadores vivos (solo ops)

## Comandos de Equipos (Lead-API)

### `/hc teams create <jugador> <número>`
Crea un equipo para un tributo con un número específico. El número debe ser un valor numérico válido.

### `/hc teams list`
Muestra todos los equipos creados.

### `/hc teams info <jugador>`
Muestra información detallada del equipo de un jugador.

### `/hc teams delete <jugador>`
Elimina el equipo de un jugador.

### `/hc teams add <jugador> <líder>`
Agrega un jugador al equipo de un líder.

### `/hc teams remove <jugador>`
Remueve un jugador de su equipo.

### `/hc teams random <tamaño>`
Crea equipos aleatorios con el tamaño especificado. Ejemplos:
- `/hc teams random 1` - Crea equipos de 1 jugador (individual)
- `/hc teams random 4` - Crea equipos de 4 jugadores
- `/hc teams random 5` - Crea equipos de 5 jugadores

**Características:**
- Distribuye automáticamente todos los jugadores online
- Excluye jugadores que ya tienen equipos
- Asigna líderes aleatoriamente
- Muestra información detallada del proceso

## Placeholders

- `%hc_tribute%` - Número de tributo del jugador
- `%hc_tributeid%` - ID del tributo
- `%hc_kills%` - Número de asesinatos
- `%hc_latest_death%` - Último jugador en morir
- `%hc_bounty%` - Tributo con recompensa activa
- `%hc_alive%` - Número total de jugadores vivos

## Dependencias

- Paper 1.21+
- PlaceholderAPI (opcional)
- Lead (opcional) - Para funcionalidades de equipos

## Instalación

1. Compila el proyecto con Gradle
2. Coloca el JAR en la carpeta `plugins`
3. Si quieres usar equipos, instala el plugin Lead
4. Reinicia el servidor

## Configuración

El plugin creará automáticamente los archivos de configuración:
- `spawnpoints.yml` - Puntos de spawn
- `chests.yml` - Configuración de cofres
- `config.yml` - Configuración general

## Funcionalidades de Equipos

### Integración con Lead-API
HungerCore ahora incluye integración completa con Lead-API, permitiendo:

- **Creación automática de equipos**: Los tributos pueden ser asignados a equipos por distrito
- **Gestión visual**: Los equipos se muestran con colores únicos en el tab y chat
- **Puntos de spawn por equipo**: Cada equipo puede tener su propio punto de aparición
- **Sistema de líderes**: Cada equipo tiene un líder que puede gestionar miembros
- **Persistencia**: Los equipos se guardan automáticamente

### Casos de uso
- **Alianzas temporales**: Los tributos pueden formar alianzas usando equipos
- **Identificación visual**: Fácil identificación de tributos por colores
- **Gestión de distritos**: Organización automática por distritos
- **Chat de equipo**: Comunicación privada entre miembros del equipo
- **Equipos aleatorios**: Creación automática de equipos con distribución aleatoria
- **Flexibilidad de tamaño**: Equipos de 1 jugador (individual) hasta equipos grandes

### Notas importantes
- **IDs numéricos**: Todos los equipos usan IDs numéricos para evitar errores de sorting en tab
- **Números automáticos**: Los equipos aleatorios usan números automáticos para evitar conflictos
- **Validación**: Se valida que todos los números de equipo sean valores numéricos válidos

## Desarrollo

### Requisitos
- Java 21+
- Gradle 8.0+

### Compilación
```bash
./gradlew build
```

### Ejecución
```bash
./gradlew runServer
```

## Ejemplos de uso

### Creación de equipos aleatorios
```bash
# Crear equipos de 4 jugadores para un evento
/hc teams random 4

# Crear equipos individuales (cada jugador por su cuenta)
/hc teams random 1

# Crear equipos de 5 jugadores para alianzas
/hc teams random 5
```

### Gestión manual de equipos
```bash
# Crear un equipo específico con número
/hc teams create PlayerName 1

# Crear un equipo con número automático (para equipos aleatorios)
/hc teams random 4

# Agregar un jugador a un equipo existente
/hc teams add NewPlayer LeaderName

# Ver información de un equipo
/hc teams info PlayerName

# Listar todos los equipos
/hc teams list
```

## Permisos

- `hc` - Acceso básico a comandos
- `hc.teams` - Acceso a comandos de equipos
- `hc.admin` - Acceso a comandos administrativos 