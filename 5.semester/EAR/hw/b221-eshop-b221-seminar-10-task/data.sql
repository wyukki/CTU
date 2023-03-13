--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: cart; Type: TABLE; Schema: public; Owner: ear; Tablespace: 
--

CREATE TABLE public.cart (
    id integer NOT NULL
);


ALTER TABLE public.cart OWNER TO ear;

--
-- Name: category; Type: TABLE; Schema: public; Owner: ear; Tablespace: 
--

CREATE TABLE public.category (
    id integer NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.category OWNER TO ear;

--
-- Name: ear_user; Type: TABLE; Schema: public; Owner: ear; Tablespace: 
--

CREATE TABLE public.ear_user (
    id integer NOT NULL,
    firstname character varying(255) NOT NULL,
    lastname character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    role character varying(255),
    username character varying(255) NOT NULL,
    cart_id integer
);


ALTER TABLE public.ear_user OWNER TO ear;

--
-- Name: item; Type: TABLE; Schema: public; Owner: ear; Tablespace: 
--

CREATE TABLE public.item (
    id integer NOT NULL,
    item_type character varying(31),
    amount integer NOT NULL,
    product_id integer NOT NULL,
    cart_id integer,
    order_id integer
);


ALTER TABLE public.item OWNER TO ear;

--
-- Name: product; Type: TABLE; Schema: public; Owner: ear; Tablespace: 
--

CREATE TABLE public.product (
    id integer NOT NULL,
    amount integer NOT NULL,
    name character varying(255) NOT NULL,
    price double precision NOT NULL,
    removed boolean
);


ALTER TABLE public.product OWNER TO ear;

--
-- Name: product_category; Type: TABLE; Schema: public; Owner: ear; Tablespace: 
--

CREATE TABLE public.product_category (
    product_id integer NOT NULL,
    categories_id integer NOT NULL
);


ALTER TABLE public.product_category OWNER TO ear;

--
-- Name: sequence; Type: TABLE; Schema: public; Owner: ear; Tablespace: 
--

CREATE TABLE public.sequence (
    seq_name character varying(50) NOT NULL,
    seq_count numeric(38,0)
);


ALTER TABLE public.sequence OWNER TO ear;

--
-- Name: shop_order; Type: TABLE; Schema: public; Owner: ear; Tablespace: 
--

CREATE TABLE public.shop_order (
    id integer NOT NULL,
    created character varying(255),
    customer_id integer NOT NULL
);


ALTER TABLE public.shop_order OWNER TO ear;

--
-- Data for Name: cart; Type: TABLE DATA; Schema: public; Owner: ear
--

INSERT INTO public.cart (id) VALUES (17);


--
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: ear
--

INSERT INTO public.category (id, name) VALUES (2, 'CPU');
INSERT INTO public.category (id, name) VALUES (7, 'Graphic card');
INSERT INTO public.category (id, name) VALUES (11, 'RAM');


--
-- Data for Name: ear_user; Type: TABLE DATA; Schema: public; Owner: ear
--

INSERT INTO public.ear_user (id, firstname, lastname, password, role, username, cart_id) VALUES (16, 'Sam', 'Fisher', '$2a$10$7Dou3yFC6NJyjWlkdAjz7OJgeMSvO7KU98FqCQejRS2eSXAo/ecLG', 'GUEST', 'fisher@example.org', 17);


--
-- Data for Name: item; Type: TABLE DATA; Schema: public; Owner: ear
--



--
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: ear
--

INSERT INTO public.product (id, amount, name, price, removed) VALUES (3, 100, 'Intel Core i7-8086K', 11922, false);
INSERT INTO public.product (id, amount, name, price, removed) VALUES (4, 150, 'Intel Core i3-7100', 3791, false);
INSERT INTO public.product (id, amount, name, price, removed) VALUES (5, 80, 'Intel Core i5-7500', 8200, false);
INSERT INTO public.product (id, amount, name, price, removed) VALUES (6, 110, 'AMD Ryzen 5 2600', 4390, false);
INSERT INTO public.product (id, amount, name, price, removed) VALUES (8, 38, 'MSI GeForce GTX 1050 Ti 4GT OC', 4490, false);
INSERT INTO public.product (id, amount, name, price, removed) VALUES (9, 75, 'MSI GeForce GTX 1080 ARMOR 8G OC', 16325, false);
INSERT INTO public.product (id, amount, name, price, removed) VALUES (10, 117, 'ASUS GeForce GTX 1060 DUAL-GTX1060-O6G', 7798, false);
INSERT INTO public.product (id, amount, name, price, removed) VALUES (12, 37, 'HyperX Fury Black 16GB DDR4 2666', 3596, false);
INSERT INTO public.product (id, amount, name, price, removed) VALUES (13, 65, 'Kingston 8GB DDR4 2400 SO-DIMM', 1853, false);
INSERT INTO public.product (id, amount, name, price, removed) VALUES (14, 66, 'Patriot Viper 3 Black Mamba 8GB DDR3 1600', 1369, false);
INSERT INTO public.product (id, amount, name, price, removed) VALUES (15, 15, 'ADATA XPG GAMMIX D10 32GB DDR4 3000', 7599, false);


--
-- Data for Name: product_category; Type: TABLE DATA; Schema: public; Owner: ear
--

INSERT INTO public.product_category (product_id, categories_id) VALUES (3, 2);
INSERT INTO public.product_category (product_id, categories_id) VALUES (4, 2);
INSERT INTO public.product_category (product_id, categories_id) VALUES (5, 2);
INSERT INTO public.product_category (product_id, categories_id) VALUES (6, 2);
INSERT INTO public.product_category (product_id, categories_id) VALUES (8, 7);
INSERT INTO public.product_category (product_id, categories_id) VALUES (9, 7);
INSERT INTO public.product_category (product_id, categories_id) VALUES (10, 7);
INSERT INTO public.product_category (product_id, categories_id) VALUES (12, 11);
INSERT INTO public.product_category (product_id, categories_id) VALUES (13, 11);
INSERT INTO public.product_category (product_id, categories_id) VALUES (14, 11);
INSERT INTO public.product_category (product_id, categories_id) VALUES (15, 11);


--
-- Data for Name: sequence; Type: TABLE DATA; Schema: public; Owner: ear
--

INSERT INTO public.sequence (seq_name, seq_count) VALUES ('SEQ_GEN', 50);


--
-- Data for Name: shop_order; Type: TABLE DATA; Schema: public; Owner: ear
--



--
-- Name: cart_pkey; Type: CONSTRAINT; Schema: public; Owner: ear; Tablespace: 
--

ALTER TABLE ONLY public.cart
    ADD CONSTRAINT cart_pkey PRIMARY KEY (id);


--
-- Name: category_pkey; Type: CONSTRAINT; Schema: public; Owner: ear; Tablespace: 
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


--
-- Name: ear_user_pkey; Type: CONSTRAINT; Schema: public; Owner: ear; Tablespace: 
--

ALTER TABLE ONLY public.ear_user
    ADD CONSTRAINT ear_user_pkey PRIMARY KEY (id);


--
-- Name: ear_user_username_key; Type: CONSTRAINT; Schema: public; Owner: ear; Tablespace: 
--

ALTER TABLE ONLY public.ear_user
    ADD CONSTRAINT ear_user_username_key UNIQUE (username);


--
-- Name: item_pkey; Type: CONSTRAINT; Schema: public; Owner: ear; Tablespace: 
--

ALTER TABLE ONLY public.item
    ADD CONSTRAINT item_pkey PRIMARY KEY (id);


--
-- Name: product_category_pkey; Type: CONSTRAINT; Schema: public; Owner: ear; Tablespace: 
--

ALTER TABLE ONLY public.product_category
    ADD CONSTRAINT product_category_pkey PRIMARY KEY (product_id, categories_id);


--
-- Name: product_pkey; Type: CONSTRAINT; Schema: public; Owner: ear; Tablespace: 
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);


--
-- Name: sequence_pkey; Type: CONSTRAINT; Schema: public; Owner: ear; Tablespace: 
--

ALTER TABLE ONLY public.sequence
    ADD CONSTRAINT sequence_pkey PRIMARY KEY (seq_name);


--
-- Name: shop_order_pkey; Type: CONSTRAINT; Schema: public; Owner: ear; Tablespace: 
--

ALTER TABLE ONLY public.shop_order
    ADD CONSTRAINT shop_order_pkey PRIMARY KEY (id);


--
-- Name: fk_ear_user_cart_id; Type: FK CONSTRAINT; Schema: public; Owner: ear
--

ALTER TABLE ONLY public.ear_user
    ADD CONSTRAINT fk_ear_user_cart_id FOREIGN KEY (cart_id) REFERENCES public.cart(id);


--
-- Name: fk_item_cart_id; Type: FK CONSTRAINT; Schema: public; Owner: ear
--

ALTER TABLE ONLY public.item
    ADD CONSTRAINT fk_item_cart_id FOREIGN KEY (cart_id) REFERENCES public.cart(id);


--
-- Name: fk_item_order_id; Type: FK CONSTRAINT; Schema: public; Owner: ear
--

ALTER TABLE ONLY public.item
    ADD CONSTRAINT fk_item_order_id FOREIGN KEY (order_id) REFERENCES public.shop_order(id);


--
-- Name: fk_item_product_id; Type: FK CONSTRAINT; Schema: public; Owner: ear
--

ALTER TABLE ONLY public.item
    ADD CONSTRAINT fk_item_product_id FOREIGN KEY (product_id) REFERENCES public.product(id);


--
-- Name: fk_product_category_categories_id; Type: FK CONSTRAINT; Schema: public; Owner: ear
--

ALTER TABLE ONLY public.product_category
    ADD CONSTRAINT fk_product_category_categories_id FOREIGN KEY (categories_id) REFERENCES public.category(id);


--
-- Name: fk_product_category_product_id; Type: FK CONSTRAINT; Schema: public; Owner: ear
--

ALTER TABLE ONLY public.product_category
    ADD CONSTRAINT fk_product_category_product_id FOREIGN KEY (product_id) REFERENCES public.product(id);


--
-- Name: fk_shop_order_customer_id; Type: FK CONSTRAINT; Schema: public; Owner: ear
--

ALTER TABLE ONLY public.shop_order
    ADD CONSTRAINT fk_shop_order_customer_id FOREIGN KEY (customer_id) REFERENCES public.ear_user(id);


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

