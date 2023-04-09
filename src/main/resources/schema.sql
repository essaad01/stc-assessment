CREATE TABLE IF NOT EXISTS public.permission_group
(
    id UUID NOT NULL,
    group_name character varying NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.item
(
    id UUID NOT NULL,
    type character varying NOT NULL,
    name character varying,
    permission_group_id UUID,
    PRIMARY KEY (id),
    CONSTRAINT fk_item_group FOREIGN KEY (permission_group_id)
    REFERENCES public.permission_group (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS public.file
(
    id UUID NOT NULL,
    "binaries" text,
    item_id UUID NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_file_item FOREIGN KEY (item_id)
    REFERENCES public.item (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS public.permission
(
    id UUID NOT NULL,
    user_email character varying,
    permission_level character varying,
    group_id UUID,
    PRIMARY KEY (id),
    CONSTRAINT fk_permission_group FOREIGN KEY (group_id)
    REFERENCES public.permission_group (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS public.space
(
    id UUID NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_space_item FOREIGN KEY (id)
    REFERENCES public.item (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS public.folder
(
    id UUID NOT NULL,
    space_id UUID,
    PRIMARY KEY (id),
    CONSTRAINT fk_folder_item FOREIGN KEY (id)
    REFERENCES public.item (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION,
    CONSTRAINT fk_folder_space FOREIGN KEY (space_id)
    REFERENCES public.space (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
);