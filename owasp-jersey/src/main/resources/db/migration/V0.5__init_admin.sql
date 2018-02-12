insert into devoxx_tia.roles (uuid, name, ui_role) values
  (x'cc934b07231a4f9891befcc09d6ad7ef', 'super', 'Administrateur'),
  (x'016a6c39389948939b004f0d41920980', 'user', 'Utilisateur');

insert into devoxx_tia.users (uuid, name, password) values
(x'59ece0a2b5ad454ba934c370330b6534', 'admin', 'sha1:64000:18:JNRTgH/TQhBoO73HZo/sSbOpxyBZLq+p:h7bzkl27X1ctg9FzNSDtjKv6'),
(x'b9288b33ce4b414eb380828e6e2cbb64', 'normal', 'sha1:64000:18:ENMMiVqUamOLnNKd8Nmm0cv1arbVspeJ:2kGiLKMQn2wxM3a/ZMWkImdW');

insert into devoxx_tia.user_roles values
  (1, 1), (1,2), (2,2);