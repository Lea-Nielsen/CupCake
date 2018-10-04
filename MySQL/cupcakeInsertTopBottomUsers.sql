USE cupcake;

INSERT INTO bottoms(bottomname, bottomprice)
VALUES
("Chocolate", 5),
("Vanilla", 5),
("Nutmeg", 5),
("Pistacio", 6),
("Almond", 7);

INSERT INTO toppings(toppingname, toppingprice)
VALUES
("Chocolate", 5),
("Blueberry", 5),
("Rasberry", 5),
("Crispy", 6),
("Strawberry", 6),
("Rum/Raisin", 7),
("Orange", 8),
("Lemon", 8),
("Blue cheese", 9);

INSERT INTO `user`(username, `password`, balance, email)
VALUES
("morten", "morten", 10000.00, "morten@test.dk"),
("lea", "lea", 10000.00, "lea@test.dk"),
("marko", "marko", 10000.00, "marko@test.dk"),
("nikolai", "nikolai", 10000.00, "nikolai@test.dk");