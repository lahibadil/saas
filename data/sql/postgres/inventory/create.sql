/**
 * inventory management
 */
create schema if not exists inventory;
set schema 'inventory';


/** tables **/
\ir tables/warehouses.sql
\ir tables/locations.sql

\ir tables/categories.sql
\ir tables/groups.sql
\ir tables/attributes.sql

\ir tables/items.sql
\ir tables/item_substitutes.sql
\ir tables/item_categories.sql
\ir tables/item_uom.sql
\ir tables/item_locations.sql


/** functions **/
\ir procs/categories/category_add.sql

\ir procs/items/item_add.sql
\ir procs/items/item_update.sql
\ir procs/items/items_all.sql


set schema 'public';