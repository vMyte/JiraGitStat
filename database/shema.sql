BEGIN;

CREATE TABLE IF NOT EXISTS public.commits
(
    id bigserial NOT NULL,
    repository_id bigint NOT NULL,
    author character varying(255) COLLATE pg_catalog."default" NOT NULL,
    message character varying(255) COLLATE pg_catalog."default",
    created_at character varying(255) COLLATE pg_catalog."default",
    hash character varying(255) COLLATE pg_catalog."default",
    total bigint,
    add bigint,
    delete bigint,
    CONSTRAINT commits_pkey PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS public.issues
(
    id bigserial NOT NULL,
    key character varying(255) COLLATE pg_catalog."default" NOT NULL,
    summary character varying(255) COLLATE pg_catalog."default",
    status character varying(255) COLLATE pg_catalog."default",
    assignee character varying(255) COLLATE pg_catalog."default",
    created_at character varying(255) COLLATE pg_catalog."default",
    updated_at character varying(255) COLLATE pg_catalog."default",
    repository_id bigint NOT NULL,
    CONSTRAINT issues_pkey PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS public.repositories
(
    id bigserial NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    owner character varying(255) COLLATE pg_catalog."default" NOT NULL,
    url character varying(255) COLLATE pg_catalog."default" NOT NULL,
    updated_at character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT repositories_pkey PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS public.statistics
(
    id bigserial NOT NULL,
    user_mapping_id bigint NOT NULL,
    metric_name character varying(255) COLLATE pg_catalog."default",
    metric_value numeric(38, 2) DEFAULT 0,
    calculated_at character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT statistics_pkey PRIMARY KEY (id),
    CONSTRAINT uniq_umid UNIQUE (user_mapping_id, metric_name)
    );

CREATE TABLE IF NOT EXISTS public.user_mapping
(
    email character varying(255) COLLATE pg_catalog."default",
    github_username character varying(255) COLLATE pg_catalog."default",
    jira_username character varying(255) COLLATE pg_catalog."default",
    id bigserial NOT NULL,
    repository_id bigint,
    CONSTRAINT user_mapping_pkey PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS public.users
(
    id bigserial NOT NULL,
    email character varying(255) COLLATE pg_catalog."default" NOT NULL,
    password character varying(255) COLLATE pg_catalog."default" NOT NULL,
    role character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT unique_email UNIQUE (email)
    );

ALTER TABLE IF EXISTS public.commits
    ADD CONSTRAINT commits_repository_id_fkey FOREIGN KEY (repository_id)
    REFERENCES public.repositories (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION;


ALTER TABLE IF EXISTS public.issues
    ADD CONSTRAINT fk_repo_id_for_issues FOREIGN KEY (repository_id)
    REFERENCES public.repositories (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION;


ALTER TABLE IF EXISTS public.statistics
    ADD CONSTRAINT fk_usermapping_for_stat FOREIGN KEY (user_mapping_id)
    REFERENCES public.user_mapping (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION;


ALTER TABLE IF EXISTS public.user_mapping
    ADD CONSTRAINT fk_repository FOREIGN KEY (repository_id)
    REFERENCES public.repositories (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE CASCADE;

END;