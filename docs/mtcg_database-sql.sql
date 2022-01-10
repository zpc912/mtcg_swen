CREATE TABLE "users" (
    "user_id" varchar(40)   NOT NULL,
    "username" varchar(12)   NOT NULL,
    "password" varchar(200)   NOT NULL,
    "elo" int   NOT NULL,
    "coins" int   NOT NULL,
    "logged" int   NOT NULL,
    CONSTRAINT "pk_users" PRIMARY KEY (
        "user_id"
     ),
    CONSTRAINT "uc_users_username" UNIQUE (
        "username"
    )
);

CREATE TABLE "cards" (
    "card_id" varchar(40)   NOT NULL,
    "name" varchar(20)   NOT NULL,
    "damage" int   NOT NULL,
    "type" varchar(10)   NOT NULL,
    "element" varchar(10)   NOT NULL,
    CONSTRAINT "pk_cards" PRIMARY KEY (
        "card_id"
     )
);

CREATE TABLE "user_cards" (
    "user_id" varchar(40)   NOT NULL,
    "card_id" varchar(40)   NOT NULL,
    "in_deck" int   NOT NULL
);

CREATE TABLE "scoreboard" (
    "score_id" varchar(40)   NOT NULL,
    "username" varchar(12)   NOT NULL,
    "elo" int   NOT NULL,
    "wins" int   NOT NULL,
    "draws" int   NOT NULL,
    "defeats" int   NOT NULL,
    "coins_spent" int   NOT NULL,
    CONSTRAINT "pk_scoreboard" PRIMARY KEY (
        "score_id"
     )
);

CREATE TABLE "trades" (
    "trade_id" varchar(40)   NOT NULL,
    "offer_user_id" varchar(40)   NOT NULL,
    "accept_user_id" varchar(40)   NOT NULL,
    "get_card_id" varchar(40)   NOT NULL,
    "give_card_id" varchar(40)   NOT NULL,
    CONSTRAINT "pk_trades" PRIMARY KEY (
        "trade_id"
     )
);

ALTER TABLE "user_cards" ADD CONSTRAINT "fk_user_cards_user_id" FOREIGN KEY("user_id")
REFERENCES "users" ("user_id");

ALTER TABLE "user_cards" ADD CONSTRAINT "fk_user_cards_card_id" FOREIGN KEY("card_id")
REFERENCES "cards" ("card_id");

ALTER TABLE "trades" ADD CONSTRAINT "fk_trades_offer_user_id" FOREIGN KEY("offer_user_id")
REFERENCES "users" ("user_id");

ALTER TABLE "trades" ADD CONSTRAINT "fk_trades_accept_user_id" FOREIGN KEY("accept_user_id")
REFERENCES "users" ("user_id");

ALTER TABLE "trades" ADD CONSTRAINT "fk_trades_get_card_id" FOREIGN KEY("get_card_id")
REFERENCES "cards" ("card_id");

ALTER TABLE "trades" ADD CONSTRAINT "fk_trades_give_card_id" FOREIGN KEY("give_card_id")
REFERENCES "cards" ("card_id");

