-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 18-08-2025 a las 05:45:24
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `alerta_segura_rd`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `alertas`
--

CREATE TABLE `alertas` (
  `id_alerta` int(11) NOT NULL,
  `titulo` varchar(20) NOT NULL,
  `tipo_alerta` varchar(20) NOT NULL,
  `nivel_alerta` varchar(20) NOT NULL,
  `sector_afectado` varchar(300) NOT NULL,
  `descripcion_detallada` varchar(1500) NOT NULL,
  `instrucciones_seguridad` varchar(2000) DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `imagen_url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

--
-- Volcado de datos para la tabla `alertas`
--

INSERT INTO `alertas` (`id_alerta`, `titulo`, `tipo_alerta`, `nivel_alerta`, `sector_afectado`, `descripcion_detallada`, `instrucciones_seguridad`, `fecha_creacion`, `imagen_url`) VALUES
(14, 'Huracan ERIN', 'Huracan', 'ALTA', 'La Altagracia, Hato Mayor, María Trinidad Sánchez, Montecristi, El Seibo, Samaná, Espaillat, Puerto Plata, Distrito Nacional y la provincia Santo Domingo.', 'La tormenta tropical Erin, que se formó el lunes en el Atlántico tropical oriental, ya se ha convertido en el primer huracán de la temporada, según informa el Centro Nacional de Huracanes (NHC) de Estados Unidos, y al momento se encuentra en categoría 3.', 'Antes del huracán\nPrepárate: Mantente informado, prepara un kit de emergencia y un botiquín.\n\nAsegura tu hogar: Protege ventanas y puertas, poda árboles y guarda objetos que puedan ser arrastrados por el viento.\n\nPlanifica: Ten un plan de evacuación y protege tus documentos importantes en un lugar seguro y a prueba de agua.\n\nDurante el huracán\nQuédate en casa: Refúgiate en una habitación interior, lejos de ventanas y puertas.\n\nNo salgas: Evita caminar o conducir en la calle.\n\nMantén la calma: Infórmate de las actualizaciones meteorológicas.\n\nDespués del huracán\nEspera: No salgas de tu refugio hasta que las autoridades lo indiquen.\n\nRevisa tu hogar: Busca daños estructurales y peligros como cables caídos o vidrios rotos.\n\nEvita peligros: No camines ni conduzcas a través de aguas de inundación y practica una buena higiene.', '2025-08-18 01:51:38', 'C:\\Users\\Grabi\\OneDrive\\Escritorio\\huracan.jpg'),
(15, 'Arbol caido', 'Evento local', 'ALTA', 'Ensanches Espalliat, Interior G entre la calle 8 y la calle Albert Thomas', 'Arbol caido producto de el Huracan ERIN, bloqueo completamente el transito por dicha calle.', 'No transitar por esa calle, otras rutas alternas pueden ser la calle 19 o la calle H', '2025-08-18 02:04:43', 'C:\\Users\\Grabi\\OneDrive\\Escritorio\\descarga.jpg'),
(16, 'Calle Inundada', 'Evento local', 'MEDIA', 'Villa mella, Res. Riobisa, C. Cesar Nicolas Penson.', 'Calle inundada debido a las fuertes lluvias causadas por el Huracan ERIN.', 'No transitar por esta calle', '2025-08-18 02:09:05', 'C:\\Users\\Grabi\\OneDrive\\Escritorio\\calle inundada.jpg'),
(17, 'Tormenta', 'Tormenta', 'BAJA', 'Santo Domingo', 'Leve tormenta supuesta a duran 2 dias.', 'Refugiarse en un lugar seguro, como una casa o un edificio con techo, tan pronto como sea posible. Si estás al aire libre, busca un refugio sólido y evita áreas expuestas como campos abiertos o estructuras altas. Mantente alejado de objetos metálicos, árboles altos y agua. En el interior, desconecta los aparatos electrónicos y evita usar teléfonos con cable.', '2025-08-18 02:12:00', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id_usuario` int(11) NOT NULL,
  `cedula` varchar(20) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `telefono` varchar(15) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `direccion` text DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `fecha_registro` datetime DEFAULT current_timestamp(),
  `estado` enum('activo','inactivo') DEFAULT 'activo'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id_usuario`, `cedula`, `nombre`, `telefono`, `email`, `direccion`, `password`, `fecha_registro`, `estado`) VALUES
(8, '123456789', 'User Comun', '8091234567', 'Usercomun@gamil.com', NULL, '123456', '2025-08-17 22:46:53', 'activo');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `alertas`
--
ALTER TABLE `alertas`
  ADD PRIMARY KEY (`id_alerta`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id_usuario`),
  ADD UNIQUE KEY `cedula` (`cedula`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `alertas`
--
ALTER TABLE `alertas`
  MODIFY `id_alerta` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id_usuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
