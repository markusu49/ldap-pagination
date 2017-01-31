<?php
/*
$ip = 'server';
$user = 'cn=admin,o=myorg';
$pw = 'password';
$base_dn = 'o=myorg';
$filter = '(objectClass=*)';
*/
require 'credentials.php';

// connect
$ds = ldap_connect($ip);
ldap_set_option($ds, LDAP_OPT_PROTOCOL_VERSION, 3);
$r = ldap_bind($ds, $user, $pw);

// retrieve unpaged result set
$sr = ldap_search($ds, $base_dn, $filter);
echo ldap_count_entries($ds, $sr)."\n"; // returns total number of entries in my directory => RIGHT

// retrieve paged result set
ldap_control_paged_result($ds, 3);
$sr = ldap_search($ds, $base_dn, $filter);
ldap_control_paged_result_response($ds, $sr, $cookie);
echo $cookie."\n";
echo ldap_count_entries($ds, $sr)."\n"; // returns 3 => RIGHT

// retrieve another unpaged result set
ldap_control_paged_result($ds, 0, false, $cookie); // disable paging
$sr = ldap_search($ds, $base_dn, $filter);
echo ldap_count_entries($ds, $sr)."\n"; // returns 0 => WRONG

// retrieve another unpaged result set
ldap_control_paged_result($ds, 0); // disable paging
$sr = ldap_search($ds, $base_dn, $filter);
echo ldap_count_entries($ds, $sr)."\n"; // returns 0 => WRONG

// reconnect to retrieve unpaged
ldap_close($ds);
$ds = ldap_connect($ip);
ldap_set_option($ds, LDAP_OPT_PROTOCOL_VERSION, 3);
$r = ldap_bind($ds, $user, $pw);
$sr = ldap_search($ds, $base_dn, $filter);
echo ldap_count_entries($ds, $sr)."\n"; // returns total number of entries in my directory => RIGHT

ldap_close($ds);
?>