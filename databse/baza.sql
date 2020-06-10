CREATE TABLE public.adresses
(
    adress_id bigint NOT NULL DEFAULT nextval('adresses_adress_id_seq'::regclass),
    street character varying(50) COLLATE pg_catalog."default",
    city character varying(50) COLLATE pg_catalog."default" NOT NULL,
    zip_code character varying(10) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT adresses_pkey PRIMARY KEY (adress_id)
)

CREATE TABLE public.categories
(
    category_id bigint NOT NULL DEFAULT nextval('categories_category_id_seq'::regclass),
    category_name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    age_group character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT categories_pkey PRIMARY KEY (category_id)
)

CREATE TABLE public.clients
(
    client_id bigint NOT NULL DEFAULT nextval('clients_client_id_seq'::regclass),
    first_name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    last_name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    email character varying(50) COLLATE pg_catalog."default" NOT NULL,
    phone_number integer NOT NULL,
    adress_id bigint,
    CONSTRAINT clients_pkey PRIMARY KEY (client_id)
)

CREATE TABLE public.stores
(
    shop_id bigint NOT NULL DEFAULT nextval('stores_shop_id_seq'::regclass),
    adress_id bigint NOT NULL,
    shop_name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT stores_pkey PRIMARY KEY (shop_id)
)

CREATE TABLE public.books
(
    book_id bigint NOT NULL DEFAULT nextval('books_book_id_seq'::regclass),
    title character varying(50) COLLATE pg_catalog."default" NOT NULL,
    author character varying(50) COLLATE pg_catalog."default" NOT NULL,
    category_id bigint,
    price integer,
    CONSTRAINT books_pkey PRIMARY KEY (book_id),
    CONSTRAINT category_book_id_fkey FOREIGN KEY (category_id)
        REFERENCES public.categories (category_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

CREATE TABLE public.transactions
(
    transaction_id bigint NOT NULL DEFAULT nextval('transactions_transaction_id_seq'::regclass),
    client_id bigint NOT NULL,
    shop_id bigint NOT NULL,
    book_id bigint NOT NULL,
    transaction_date date,
    price integer NOT NULL,
    CONSTRAINT transactions_pkey PRIMARY KEY (transaction_id)
)