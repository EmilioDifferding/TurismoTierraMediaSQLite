BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "usuario" (
	"usuario_id"	INTEGER NOT NULL UNIQUE,
	"nombre"	TEXT NOT NULL UNIQUE,
	"atraccion_favorita"	INTEGER,
	"presupuesto"	NUMERIC DEFAULT 0,
	"tiempo_disponible"	NUMERIC DEFAULT 0,
	PRIMARY KEY("usuario_id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "itinerario" (
	"itinerario_id"	INTEGER NOT NULL UNIQUE,
	"fecha"	NUMERIC NOT NULL,
	"usuario_id"	INTEGER NOT NULL,
	PRIMARY KEY("itinerario_id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "tipo_atraccion" (
	"tipo_atraccion_id"	INTEGER NOT NULL UNIQUE,
	"nombre"	TEXT NOT NULL,
	PRIMARY KEY("tipo_atraccion_id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "promocion_atraccion" (
	"atraccion_id"	INTEGER NOT NULL,
	"promocion_id"	INTEGER NOT NULL,
	FOREIGN KEY("promocion_id") REFERENCES "promocion"("promocion_id"),
	PRIMARY KEY("atraccion_id","promocion_id")
);
CREATE TABLE IF NOT EXISTS "tipo_descuento_promocion" (
	"tipo_descuento_promocion_id"	INTEGER NOT NULL UNIQUE,
	"nombre"	TEXT NOT NULL,
	PRIMARY KEY("tipo_descuento_promocion_id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "promocion" (
	"promocion_id"	INTEGER NOT NULL UNIQUE,
	"nombre"	TEXT NOT NULL,
	"descripcion"	TEXT DEFAULT 'Sin descripcion',
	"descuento"	TEXT DEFAULT 0,
	"costo"	INTEGER DEFAULT 0,
	"tipo_promocion"	INTEGER DEFAULT 0,
	PRIMARY KEY("promocion_id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "atraccion" (
	"atraccion_id"	INTEGER NOT NULL UNIQUE,
	"nombre"	TEXT NOT NULL UNIQUE,
	"descripcion"	TEXT,
	"costo"	INTEGER NOT NULL DEFAULT 0,
	"tiempo"	NUMERIC NOT NULL,
	"cupo"	INTEGER NOT NULL DEFAULT 0,
	"tipo_id"	INTEGER NOT NULL,
	PRIMARY KEY("atraccion_id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "detalle_itinerario" (
	"itinerario_id"	INTEGER NOT NULL,
	"detalle_itinerario_id"	INTEGER NOT NULL,
	"atraccion_id"	INTEGER NOT NULL,
	"promo_id"	INTEGER,
	FOREIGN KEY("itinerario_id") REFERENCES "itinerario"("itinerario_id"),
	PRIMARY KEY("detalle_itinerario_id")
);
INSERT INTO "usuario" VALUES (1,'Frodo',1,10,8);
INSERT INTO "usuario" VALUES (2,'Gandalf',3,100,5);
INSERT INTO "usuario" VALUES (3,'Sam',2,36,8);
INSERT INTO "tipo_atraccion" VALUES (1,'Aventura');
INSERT INTO "tipo_atraccion" VALUES (2,'Degustación');
INSERT INTO "tipo_atraccion" VALUES (3,'Paisaje');
INSERT INTO "promocion_atraccion" VALUES (8,1);
INSERT INTO "promocion_atraccion" VALUES (4,1);
INSERT INTO "promocion_atraccion" VALUES (2,2);
INSERT INTO "promocion_atraccion" VALUES (5,2);
INSERT INTO "promocion_atraccion" VALUES (7,2);
INSERT INTO "promocion_atraccion" VALUES (6,3);
INSERT INTO "promocion_atraccion" VALUES (3,3);
INSERT INTO "tipo_descuento_promocion" VALUES (1,'Porcentual');
INSERT INTO "tipo_descuento_promocion" VALUES (2,'AxB');
INSERT INTO "tipo_descuento_promocion" VALUES (3,'Absoluto');
INSERT INTO "promocion" VALUES (1,'Pack aventura','Bosque Negro y Mordor con un 20% de descuento si se compran ambas.','20',22,1);
INSERT INTO "promocion" VALUES (2,'Pack paisajes','Comprando Minas Tirith y el Abismo de Helm, Erebor es gratis.','Erebor',40,2);
INSERT INTO "promocion" VALUES (3,'Pack degustación','Lothlórien y La Comarca a 36 monedas.','36',36,3);
INSERT INTO "atraccion" VALUES (1,'Moria',NULL,10,2,6,1);
INSERT INTO "atraccion" VALUES (2,'Minas Tirith',NULL,5,2.5,25,3);
INSERT INTO "atraccion" VALUES (3,'La Comarca',NULL,3,2.5,150,2);
INSERT INTO "atraccion" VALUES (4,'Mordor',NULL,25,3,4,1);
INSERT INTO "atraccion" VALUES (5,'Abismo Helm',NULL,5,2,15,3);
INSERT INTO "atraccion" VALUES (6,'Lothlorien',NULL,35,1,30,2);
INSERT INTO "atraccion" VALUES (7,'Erebor',NULL,12,3,32,3);
INSERT INTO "atraccion" VALUES (8,'Bosque Negro',NULL,3,4,12,1);
COMMIT;
