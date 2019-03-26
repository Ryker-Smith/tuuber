#!/usr/bin/perl
use strict;

use constant progamVersion => "0.9.2";

use CGI;
use DBI;
use CGI::Carp ("fatalsToBrowser");

# are these used?:
use URI qw();
use URI::QueryParam qw();
use JSON;

print "Content-type: text/html\n\n";

my $cgi = CGI->new;
$cgi->header( -type => "application/json", -charset => "utf-8" );

my ${quoteJSON}='"';

# set-up the database connection. Configgy bits are in external config file
our ( $db, $dbconn, $dbhost, $dbuser, $dbpasswd, $dbh );
dbPrepare();

# read key CGI variables
my $action   = $cgi->param('action');
if ($action eq "") {
  $action = $cgi->param('method'); # changed to using 'method' in later versions of the API
}
my $entity   = $cgi->param('entity');
my $cmd      = $cgi->param('cmd');
my $showHtml = $cgi->param('showHtml');
my $pretty = $cgi->param('pretty');

if ( $showHtml eq "" ) {
    my $showHtml = 0;
}

# the things/entities in the system that CGI variables nay refere to
use constant person => 'PERSON';
use constant route  => 'ROUTE';
use constant pool   => 'POOL';
use constant town   => 'TOWN';
use constant chat   => 'CHAT';
use constant match   => 'MATCH';

# the actions to take with things/entities
use constant POST   => 'POST';
use constant PUT    => 'PUT';
use constant GET    => 'GET';
use constant DELETE => 'DELETE';
use constant LIST   => 'LIST';

# special actions, not necessarily for manipulating/changing data
use constant login    => 'LOGIN';
use constant register => 'REGISTER';
use constant debug    => 'DEBUG';
use constant version  => 'VER';
use constant passwordChange => 'CHPWD';

my $logfile = "/home/public/tuber.dat";

my @data = $cgi->param;

open FILE, ">> $logfile";
use DateTime;
my $dt = DateTime->now;
my $time = join ' ', $dt->ymd, $dt->hms;
dbg(    "\n<b>-----New Query-----<b>\n"
      . $time
      . "\nFrom address: "
      . $ENV{REMOTE_ADDR}
      . "\nWith browser: "
      . $ENV{HTTP_USER_AGENT}
      . "\nParameters: " );

foreach (@data) {
    print FILE "($_=" . $cgi->param($_) . ") ";
}
print FILE "\n";

my $sessionID = $cgi->param('sessionID');

# print $cmd;
#===== Commands
if ( uc $cmd eq version ) {
    print "Version: " . progamVersion;
    exit;
}
elsif ( uc $cmd eq login ) {
    print loginCheck();
    exit;
}
elsif ( uc $cmd eq passwordChange ) {
    print doPasswordChange();
    exit;
}
elsif ( $entity eq "" ) {
    $entity = debug;
}

if ( !validSessionID( $sessionID ) ) {
  print "{ ${quoteJSON}result${quoteJSON}:${quoteJSON}Feck${quoteJSON} }";
  exit;
}
#===== End commands

dbg($action);
dbg( $ENV{'QUERY_STRING'} );

if ( uc $entity eq person ) {
    persons();
}
elsif ( uc $entity eq route ) {
    routes();
}
elsif ( uc $entity eq pool ) {
    pools();
}
elsif ( uc $entity eq town ) {
    towns();
}
elsif ( uc $entity eq chat ) {
    chats();
}
elsif ( uc $entity eq debug ) {
    debugScreen();
}
elsif ( uc $entity eq register ) {
    registerPerson();
}
elsif ( uc $entity eq match ) {
    matches();
}
else {
    dbg('ERR: entity type not matched');
}
dbg('-----END-----');
close FILE;

sub person_GET {

    # usage:   person_GET(pID)
    # purpose: get details of one person, based on pID
    my $pID   = shift;
    my $email = shift;
    my ( $q, $param );
    if ( $email eq "" ) {
        $q =
          "SELECT pID, first, family, phone, email FROM persons WHERE pID=?;";
        $param = $pID;
    }
    else {
        $q =
"SELECT pID, first, family, phone, email FROM persons WHERE email LIKE ?;";
        $param = $email;
    }
    my $qh = $dbh->prepare($q);
    $qh->execute($param);
    my $result = $qh->fetchrow_hashref();
    my $json   = "{";
    foreach ( keys %$result ) {
#         $$result{$_} = deApostrophisise( $$result{$_} );
        $json .= "${quoteJSON}$_${quoteJSON}:${quoteJSON}" . $$result{$_} . "${quoteJSON},";
    }
    $json =~ s/,$//;    #remove final comma
    $json .= "}";
    return $json;
}

sub persons_ID_list {

    # purpose: get list of pID's
    # usage:   persons_ID_list()
    my $pID = shift;
    my $q   = "SELECT pID FROM persons;";
    my $qh  = $dbh->prepare($q);
    $qh->execute();
    my $result = $qh->fetchall_arrayref();

    # Cast address as array
    return @{$result};
}

sub persons () {
    my ( $q, $result );

    # read the CGI input from the browser request
    my $pID    = $cgi->param('pID');
    my $first  = deApostrophisise( $cgi->param('first') );
    my $family = deApostrophisise( $cgi->param('family') );
    my $phone  = $cgi->param('phone');
    my $email  = $cgi->param('email');

    if ( uc $action eq POST ) {
        my $password=$cgi->param('pw');
        if ($password ne "") {
        $q =
          "INSERT INTO persons (first, family, phone, email, password) VALUES (?, ?, ?, ?, ?);";
        }
        else {$q =
          "INSERT INTO persons (first, family, phone, email) VALUES (?, ?, ?, ?);";
        }
#         print $q;
        my $qh = $dbh->prepare($q);
        if ($password ne "") {
          $result = $qh->execute( $first, $family, $phone, $email, $password )
        }
        else {
          $result = $qh->execute( $first, $family, $phone, $email );
        }
        dbg("Created person with pID $result");
        my $json = "{${quoteJSON}result${quoteJSON}:${quoteJSON}";
        if ( $result > 0 ) {
            $json .= "OK";
        }
        else {
            $json .= "Error (possibly: user already exists)";
        }
        $json .= "${quoteJSON},${quoteJSON}pID${quoteJSON}:${quoteJSON}" . $qh->{mysql_insertid} . "${quoteJSON},${quoteJSON}sessionID${quoteJSON}:" . makeSessionID() . "}";
        print $json;
    }
    elsif ( uc $action eq PUT ) {
        $q =
          "UPDATE persons SET first=?, family=?, phone=?, email=? WHERE pID=?;";
        my $qh = $dbh->prepare($q);
        $result = $qh->execute( $first, $family, $phone, $email, $pID );
        $result = "OK" if ( $result == 1 );
        my $json = "{${quoteJSON}result${quoteJSON}:${quoteJSON}" . $result . "${quoteJSON}}";
        print $json;
    }
    elsif ( uc $action eq GET ) {
        my $json = person_GET( $pID, $email );
        print $json;
        print FILE $json;
    }
    elsif ( uc $action eq DELETE ) {
        $q = "DELETE FROM persons WHERE pID=?;";
        my $qh = $dbh->prepare($q);
        $result = $qh->execute($pID);
        $result = "OK" if ( $result == 1 );
        my $json = "{${quoteJSON}result${quoteJSON}:${quoteJSON}" . $result . "${quoteJSON}}";
        print $json;
    }
    elsif ( uc $action eq LIST ) {
        if ( !$showHtml ) {    # if not showing html, do the JSON dump
            my @list = persons_ID_list();
            print " { ${quoteJSON}persons${quoteJSON}:[";
            foreach (@list) {
                my $text= person_GET(@$_);
                print $text;
                if ( \$_ != \$list[-1] ) {
                    # if not at end of list
                    if ($text ne '') {
                        print ",";
                    }
                }
            }
            print "]} ";      # end the JSON array
            return;            # end of JSON dump, return;
        }

        # do non-JSON dump
        my @columns = listTableFields('persons');
        @columns = grep { $_ ne 'password' } @columns;
        foreach (@columns) { $_ = $_ . ','; }
        chop( $columns[ scalar @columns - 1 ] );    # strip trainling comma
        $q = "SELECT @columns FROM persons;";
        my $qh = $dbh->prepare($q);
        $qh->execute();
        my $file;

        if ($showHtml) {
            $file = "\n<table><tr>";
            my @fieldNames = listTableFields('persons');
            foreach (@fieldNames) {
                $file .= "<td><strong>$_</strong></td>";
            }
            $file .= "</tr>";
        }
        else {
            $file = "";
        }
        while ( my @data = $qh->fetchrow_array() ) {
            if ($showHtml) {
                $file .= "<tr>";
            }
            foreach (@data) {
                if ($showHtml) {
                    $file .= "<td> $_ </td>";
                }
                else {
                    $file .= $_ . "::";
                }
            }
            if ($showHtml) {
                $file .= "</tr>";
            }
            $file =~ s/::$//;    #remove final separator
            $file .= "\n";
        }
        if ($showHtml) {
            $file .= "</table>\n";
        }
        if ( !$showHtml ) {
            dbg($file);
        }
        print $file;
    }
    else {
    }
}

sub route_GET {

 # usage:   route_GET(rID)
 # purpose: get details of one person, based on pID
 #     my ($rID, $pID, $origin, $destination, $driver) = @_; #get all parameters
 #     print "[$rID], [$pID], [$origin], [$destination], [$driver]";
    my $rID = shift;
    my ( $q, $param, $result );
    $q =
"SELECT routes.*, CONCAT(persons.first,' ', persons.family) AS personName FROM routes JOIN persons ON persons.pID=routes.pID WHERE rID=?;";
    my $qh   = $dbh->prepare($q);
    my $json = "";
    $qh->execute($rID);
    $result = $qh->fetchrow_hashref();

    foreach ( keys %$result ) {
#         $$result{$_} = deApostrophisise( $$result{$_} );
        $json .= "${quoteJSON}". reApostrophisise($_). "${quoteJSON}:${quoteJSON}" . $$result{reApostrophisise($_)} . "${quoteJSON},";
    }
    $json =~ s/,$//;    #remove final comma
    if ($json ne "") {
      $json =  "{" . $json . "}";
    }
    return $json;
}

sub routes_ID_list {

    # purpose: get list of rID's (routes)
    # usage:   routes_ID_list()
    #     my $rID = shift;
    my ( $rID, $pID, $origin, $destination, $day, $driver ) = @_;  #get all parameters

    #     print "[$rID], [$pID], [$origin], [$destination], [$driver]";
    my ( $q, $param, $result );
    $q = "SELECT rID FROM routes ";
    if ( $pID ne "" ) {
        $q .= "WHERE pID=?";
        $param = $pID;
    }
    elsif ( $origin ne "" ) {
        $q .= "WHERE origin=?";
        $param = $origin;
    }
    elsif ( $destination ne "" ) {
        $q .= "WHERE destination = ?";
        $param = $destination;
    }
    elsif ( $driver ne "" ) {
        $q .= "WHERE driver LIKE ?";
        $param = $driver;
    }
    elsif ( defined $rID ) {
        $q .= "WHERE rID=?";
        $param = $rID;
    }
    elsif ( defined $day ) {
        $q .= "WHERE day=?";
        $param = $day;
    }
    else {
#       print "F";
    }
    $q .= ";";    # probably unnecessary here
#     print "[$q]";
    my $qh = $dbh->prepare($q);

    #     print $q;
    if ( defined $param ) {
        $qh->execute($param);
    }
    elsif ( defined $rID ) {
        $qh->execute($rID);
    }
    else {
        $qh->execute();
    }
    my $result = $qh->fetchall_arrayref();

    # Cast address as array
    return @{$result};
}

sub routes {
    my ( $q, $result );

    # read the CGI input from the browser request
    my $rID         = $cgi->param('rID');
    my $origin      = $cgi->param('origin');
    my $destination = $cgi->param('destination');
    my $mon         = $cgi->param('mon');
    my $tues        = $cgi->param('tues');
    my $weds        = $cgi->param('weds');
    my $thurs       = $cgi->param('thurs');
    my $fri         = $cgi->param('fri');
    my $pID         = $cgi->param('pID');
    my $driver      = $cgi->param('driver');
    my $day         = $cgi->param('day');
    if ($mon eq "") {$mon="N";};
    if ($tues eq "") {$tues="N";};
    if ($weds eq "") {$weds="N";};
    if ($thurs eq "") {$thurs="N";};
    if ($fri eq "") {$fri="N";};

    if ( uc $action eq POST ) {
        $q =
"INSERT INTO routes (origin, destination, mon, tues, weds, thurs, fri, pID, driver, day) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        my $qh = $dbh->prepare($q);
        $result = $qh->execute(
            $origin, $destination, $mon, $tues, $weds,
            $thurs,  $fri,         $pID, $driver, $day
        );
        print "[$day]";
        my $json = "{${quoteJSON} rID${quoteJSON}:${quoteJSON}" . $qh->{mysql_insertid} . "${quoteJSON}}";
        print $json;
    }
    elsif ( uc $action eq PUT ) {
        $q =
"UPDATE routes SET origin=?, destination=?, mon=?, tues=?, weds=?, thurs=?, fri=?, pID=?, driver=?, day=? WHERE rID=?;";
        my $qh = $dbh->prepare($q);
        $result = $qh->execute(
            $origin, $destination, $mon, $tues,   $weds,
            $thurs,  $fri,         $pID, $driver, $rID, $day
        );
        $result = "OK" if ( $result == 1 );
        my $json = "{${quoteJSON} result${quoteJSON}:${quoteJSON}" . $result . "${quoteJSON}}";
        print $json;
    }
    elsif ( uc $action eq GET ) {
        my $json = route_GET( $rID, $pID, $origin, $destination, $driver );
        print $json;
        print FILE $json;
    }
    elsif ( uc $action eq DELETE ) {
        $q = "DELETE FROM routes WHERE rID=?;";
        my $qh = $dbh->prepare($q);
        $result = $qh->execute($rID);
        $result = "OK" if ( $result == 1 );

        my $json = "{${quoteJSON}result${quoteJSON}:${quoteJSON}" . $result . "${quoteJSON}}";
        print $json;
    }
    elsif ( uc $action eq LIST ) {

        # PATCHY BIT
        #
#         if ( defined $cgi->param("sql") ) {
#             print "<[", $cgi->param("sql"), "]>";
#             patch_sql( $cgi->param("sql") );
#         }
        #
        # !PATCHY BIT
        if ( !$showHtml ) {    # if not showing html, do the JSON dump
            my @list =
              routes_ID_list( $rID, $pID, $origin, $destination, $driver );
            print " {${quoteJSON}routes${quoteJSON}:[";
#             print "[@list]";
            foreach (@list) {
                my $text= route_GET(@$_);
                print $text;
#                 print "[ @$_ ]";
              if ( \$_ != \$list[-1] ) {
                    # if not at end of list
                    if ($text ne '') {
                        print ",";
                    }
                }
            }
            print "]} ";      # end the JSON array
            return;            # end of JSON dump, return;
        }

        # do non-JSON dump

        $q = "SELECT * FROM routes;";
        my $qh = $dbh->prepare($q);
        $qh->execute();
        my $file;
        if ($showHtml) {
            $file = "\n<table><tr>";
            my @fieldNames = listTableFields('routes');
            foreach (@fieldNames) {
                $file .= "<td><strong>$_</strong></td>";
            }
            $file .= "</tr>";
        }
        else {
            $file = "";
        }
        while ( my @data = $qh->fetchrow_array() ) {
            if ($showHtml) {
                $file .= "<tr>";
            }
            foreach (@data) {
                if ($showHtml) {
                    $file .= "<td> $_ </td>";
                }
                else {
                    $file .= $_ . "::";
                }
            }
            if ($showHtml) {
                $file .= "</tr>";
            }
            $file =~ s/::$//;    #remove final separator
            $file .= "\n";
        }
        if ($showHtml) {
            $file .= "</table>\n";
        }
        if ( !$showHtml ) {
            dbg($file);
        }
        print $file;
    }
    else {
    }
}

sub pool_GET {

    # usage:   pool_GET(pID)
    # purpose: get details of one person, based on pID
    my $pool_ID = shift;
    my $q       = "SELECT * FROM pools WHERE pool_ID=?;";
    my $qh      = $dbh->prepare($q);
    $qh->execute($pool_ID);
    my $result = $qh->fetchrow_hashref();
    my $json   = "{";
    foreach ( keys %$result ) {
#         $$result{$_} = deApostrophisise( $$result{$_} );
        $json .= "${quoteJSON}$_${quoteJSON}:${quoteJSON}" . $$result{$_} . "${quoteJSON},";
    }
    $json =~ s/,$//;    #remove final comma
    $json .= "}";
    return $json;
}

sub pools_ID_list {

    # purpose: get list of rID's (routes)
    # usage:   routes_ID_list()
    my $pool_ID = shift;
    my $q       = "SELECT pool_ID FROM pools;";
    my $qh      = $dbh->prepare($q);
    $qh->execute();
    my $result = $qh->fetchall_arrayref();

    # Cast address as array
    return @{$result};
}

sub pools {
    my ( $q, $result );
    my $pool_ID       = $cgi->param('pool_ID');
    my $driver_pID    = $cgi->param('driver_pID');
    my $navigator_pID = $cgi->param('navigator_pID');
    my $rID           = $cgi->param('rID');

    if ( uc $action eq POST ) {
        $q =
"INSERT INTO pools (driver_pID, navigator_pID, rID) VALUES (?, ?, ?);";
        my $qh = $dbh->prepare($q);
        $result = $qh->execute( $driver_pID, $navigator_pID, $rID );
        my $pool_ID = $dbh->{mysql_insertid};
        dbg("Created pool with ID: $pool_ID");
        print "{${quoteJSON}pool_IDv:${quoteJSON}$pool_ID${quoteJSON}}";
    }
    elsif ( uc $action eq PUT ) {
        $q =
"UPDATE pools SET driver_pID=?, navigator_pID=?, rID=? WHERE pool_ID=?;";
        my $qh = $dbh->prepare($q);
        $result = $qh->execute( $driver_pID, $navigator_pID, $rID, $pool_ID );
        $result = "OK" if ( $result == 1 );
        my $json = "{${quoteJSON}result${quoteJSON}:${quoteJSON}" . $result . "${quoteJSON}}";
        print $json;
    }
    elsif ( uc $action eq GET ) {
        $q =
          "SELECT driver_pID, navigator_pID, rID FROM pools WHERE pool_ID=?;";
        my $qh = $dbh->prepare($q);
        $qh->execute($pool_ID);
        $result = $qh->fetchrow_hashref();

        my $json = "{";
        foreach ( keys %$result ) {
#             $$result{$_} = deApostrophisise( $$result{$_} );
            $json .= "${quoteJSON}$_${quoteJSON}:${quoteJSON}" . $$result{$_} . "${quoteJSON},";
        }
        $json =~ s/,$//;    #remove final comma
        $json .= "}";
        print $json;
        print FILE $json;
    }
    elsif ( uc $action eq DELETE ) {
        $q = "DELETE FROM pools WHERE pool_ID=?;";
        my $qh = $dbh->prepare($q);
        $result = $qh->execute($pool_ID);
        $result = "OK" if ( $result == 1 );
        my $json = "{${quoteJSON}result${quoteJSON}:${quoteJSON}" . $result . "${quoteJSON}}";
        print $json;
    }
    elsif ( uc $action eq LIST ) {

        if ( !$showHtml ) {    # if not showing html, do the JSON dump
            my @list = pools_ID_list();
            print "{${quoteJSON}pools${quoteJSON}:[";
            foreach (@list) {
                my $text = pool_GET(@$_);
                print $text;
                if ( \$_ != \$list[-1] ) {
                    if ($text ne '') {
                        # if not at end of list
                        print ",";
                    }
                }
            }
            print "]}";      # end the JSON array
            return;            # end of JSON dump, return;
        }

        # do non-JSON dump
        $q = "SELECT * FROM pools;";
        my $qh = $dbh->prepare($q);
        $qh->execute();
        my $file;
        if ($showHtml) {
            $file = "\n<table><tr>";
            my @fieldNames = listTableFields('pools');
            foreach (@fieldNames) {
                $file .= "<td><strong>$_</strong></td>";
            }
            $file .= "</tr>";
        }
        else {
            $file = "";
        }
        while ( my @data = $qh->fetchrow_array() ) {
            foreach (@data) {
                if ($showHtml) {
                    $file .= "<td> $_ </td>";
                }
                else {
                    $file .= $_ . "::";
                }
            }
            if ($showHtml) {
                $file .= "</tr>";
            }
            $file =~ s/::$//;    #remove final separator
            $file .= "\n";

        }
        if ($showHtml) {
            $file .= "</table>\n";
        }
        if ( !$showHtml ) {
            dbg($file);
        }
        print $file;
    }
    else {
    }
}

sub town_GET {

    # usage:   town_GET(pID)
    # purpose: get details of one town, based on tID
    my $tID = shift;
    my $q =
"SELECT tId, name, CONCAT(barony,', ', civilParish,', ', poorLawUnion) AS location FROM towns WHERE tID=?;";
    my $qh = $dbh->prepare($q);
    $qh->execute($tID);
    my $result = $qh->fetchrow_hashref();
    my $json   = "{";
    foreach ( keys %$result ) {
#         $$result{$_} = deApostrophisise( $$result{$_} );
        $json .= "${quoteJSON}$_${quoteJSON}:${quoteJSON}" . $$result{$_} . "${quoteJSON},";
    }
    $json =~ s/,$//;    #remove final comma and space
    $json .= "}";
    return $json;
}

sub towns_ID_list {

    # purpose: get list of town tID's
    # usage:   towns_ID_list()
    my $q  = "SELECT tID FROM towns WHERE acres=-1;";
    my $qh = $dbh->prepare($q);
    $qh->execute();
    my $result = $qh->fetchall_arrayref();

    # Cast address as array
    return @{$result};
}

sub towns {
    my ( $q, $result );
    my $tID = $cgi->param('tID');
    if ( uc $action eq GET ) {
        $q = "SELECT * FROM towns WHERE tID=?;";
        my $qh = $dbh->prepare($q);
        $qh->execute($tID);
        $result = $qh->fetchrow_hashref();

        my $json = "{";
        foreach ( keys %$result ) {
#             $$result{$_} = deApostrophisise( $$result{$_} );
            $json .= "${quoteJSON}$_${quoteJSON}:${quoteJSON}" . $$result{$_} . "${quoteJSON},";
        }
        $json =~ s/,$//;    #remove final comma
        $json .= "}";
        print $json;
        print FILE $json;
    }
    if ( uc $action eq LIST ) {
        if ( !$showHtml ) {    # if not showing html, do the JSON dump
            my @list = towns_ID_list();
            print " {${quoteJSON}towns${quoteJSON}:[";
            foreach (@list) {
                my $text = town_GET(@$_);
                print $text;
                if ( \$_ != \$list[-1] ) {
                    if ($text ne '') {
                        # if not at end of list
                        print ",";
                    }
                }
            }
            print "]}";      # end the JSON array
            return;            # end of JSON dump, return;
        }

        # do non-JSON dump
        $q =
"SELECT tID, CONCAT( name, ', ', barony, ', ', civilParish) FROM towns WHERE acres=-1;";
        my $qh = $dbh->prepare($q);
        $qh->execute();
        my $file;
        if ($showHtml) {
            $file = "\n<table><tr>";
            my @fieldNames = listTableFields('towns');
            foreach (@fieldNames) {
                $file .= "<td><strong>$_</strong></td>";
            }
            $file .= "</tr>";
        }
        else {
            $file = "";
        }
        while ( my @data = $qh->fetchrow_array() ) {
            foreach (@data) {
                if ($showHtml) {
                    $file .= "<td> $_ </td>";
                }
                else {
                    $file .= $_ . "::";
                }
            }
            if ($showHtml) {
                $file .= "</tr>";
            }
            $file =~ s/::$//;    #remove final separator
            $file .= "\n";

        }
        if ($showHtml) {
            $file .= "</table>\n";
        }
        if ( !$showHtml ) {
            dbg($file);
        }
        print $file;
    }
    else {
    }
}

sub chatLine_GET {

    # usage:
    # purpose: get details of one line of chat, based on pID
    my $line_ID = shift;
#     "SELECT
#      chats.*, table1.respondentName, table2.initiatorName
#      FROM
#      chats,
#      (SELECT CONCAT(persons.first,' ', persons.family) AS respondentName FROM chats JOIN persons ON (persons.pID=chats.respondent_pID) WHERE line_ID=?) AS table1, 
#      (SELECT CONCAT(persons.first,' ', persons.family) AS initiatorName FROM chats JOIN persons ON (persons.pID=chats.initiator_pID) WHERE line_ID=?) as table2
#      WHERE line_ID=?";
#      print "[$q]";
    my $who;
    if ($cgi->param('initiator_pID') eq "") {
      $who="initiator_pID";
    }
    else {
      $who="respondent_pID";
    }
    my $q =    
         "SELECT persons.first, persons.family, chats.* FROM chats JOIN persons ON persons.pID=chats.$who WHERE line_ID=? ORDER BY chats.line_ID ASC";
    my $qh      = $dbh->prepare($q);
    $qh->execute($line_ID);
    my $result = $qh->fetchrow_hashref();
    my $json   = "{";
    foreach ( keys %$result ) {
        $$result{$_} = deApostrophisise( $$result{$_} );
        $json .= "${quoteJSON}$_${quoteJSON}:${quoteJSON}" . $$result{$_} . "${quoteJSON},";
    }
    $json =~ s/,$//;    #remove final comma
    my $addInTheSQL_InPanic="";#",${quoteJSON}sql${quoteJSON}:${quoteJSON}$q${quoteJSON}";
    $json .= "$addInTheSQL_InPanic}";
    return $json;
}

sub chat_ID_list {

    # purpose: get list of Chat line_ID's
    # usage:   chats_ID_list()
    #    
    my ( $initiator_pID, $respondent_pID, $status) = @_;  #get all parameters

    #     print "[$rID], [$pID], [$origin], [$destination], [$driver]";
    my ( $q, @params, $result, $q_reverse );
    
    $q="";
    $q_reverse="";
    if ( ( $initiator_pID ne "" ) && ( $respondent_pID ne "" ) ) {
      $q_reverse = "((initiator_pID=? AND respondent_pID=?) OR (respondent_pID=? AND initiator_pID=?))";
      # reverse order for counter-chat
      push @params, $respondent_pID;
      push @params, $initiator_pID;
      push @params, $respondent_pID;
      push @params, $initiator_pID;
      $q = $q_reverse;
    }
    else {
      if ( $initiator_pID ne "" ) {
  #         if ($q ne "") {
  #           $q .= " AND ";
  #         }
          $q .= " initiator_pID=? ";
          push @params, $initiator_pID;
      }
      if ( $respondent_pID ne "" ) {
          if ($q ne "") {
            $q .= " AND ";
          }
          $q .= " respondent_pID=? ";
          push @params, $respondent_pID;
      }
    }
    # now add the status if reqd
    if ( $status ne "" ) {
        if ($q ne "") {
          $q .= " AND ";
        }
        $q .= " status LIKE ? ";
        push @params, $status;
    }
    if ($q ne "") {
      $q = " WHERE (" . $q . ")";
    }
#    $q = "SELECT first, family, line_ID FROM chats JOIN persons ON persons.pID=initiator_pID " . $q . " ORDER BY timestamp ASC;"; 
    $q = "SELECT line_ID FROM chats " . $q . " ORDER BY timestamp ASC;"; 
#       print "[$q]";
    my $qh = $dbh->prepare($q);
    $qh->execute(@params);
    my $result = $qh->fetchall_arrayref();
#  print "[$q]<br>\n";
    # Cast address as array
    return @{$result};
}

sub chats {
    my ( $q, $result );
    my $line_ID   = $cgi->param('line_ID');
    my $my_pID    = $cgi->param('initiator_pID');
    my $other_pID = $cgi->param('respondent_pID');
    my $text      = $cgi->param('text');
    my $status    = $cgi->param('status');

    if ( uc $action eq POST ) {
        $q =
"INSERT INTO chats (initiator_pID, respondent_pID, text, status) VALUES (?, ?, ?, ?);";
        my $qh = $dbh->prepare($q);
        $result = $qh->execute( $my_pID, $other_pID, $text, $status );
        $line_ID = $dbh->{mysql_insertid};
        dbg("Created line with ID: $line_ID");
        print "{${quoteJSON}line_ID${quoteJSON}:${quoteJSON}$line_ID${quoteJSON}}";
    }
    elsif ( uc $action eq PUT ) {
        $q =
"UPDATE chats SET initiator_pID=?, respondent_pID=?, text=?, status=? WHERE line_ID=?;";
        my $qh = $dbh->prepare($q);
        $result = $qh->execute( $my_pID, $other_pID, $text, $status, $line_ID );
        $result = "OK" if ( $result == 1 );
        my $json = "{${quoteJSON}result${quoteJSON}:${quoteJSON}" . $result . "${quoteJSON}}";
        print $json;
    }
    elsif ( uc $action eq GET ) {
        $q =
          "SELECT initiator_pID, respondent_pID, status, text FROM chats WHERE line_ID=?;";
        my $qh = $dbh->prepare($q);
        $qh->execute($line_ID);
        $result = $qh->fetchrow_hashref();

        my $json = "{";
        foreach ( keys %$result ) {
            $$result{$_} = deApostrophisise( $$result{$_} );
            $json .= "${quoteJSON}$_${quoteJSON}:${quoteJSON}" . $$result{$_} . "${quoteJSON},";
        }
        $json =~ s/,$//;    #remove final comma
        $json .= "}";
        print $json;
        print FILE $json;
    }
    elsif ( uc $action eq DELETE ) {
        $q = "DELETE FROM chats WHERE line_ID=?;";
        my $qh = $dbh->prepare($q);
        $result = $qh->execute($line_ID);
        $result = "OK" if ( $result == 1 );
        my $json = "{${quoteJSON}result${quoteJSON}:${quoteJSON}" . $result . "${quoteJSON}}";
        print $json;
    }
    elsif ( uc $action eq LIST ) {
        my @list = chat_ID_list( $my_pID, $other_pID, $status );
        if ( !$showHtml ) {    # if not showing html, do the JSON dump
            
            print " {${quoteJSON}chat${quoteJSON}:[";
            foreach (@list) {
                my $text = chatLine_GET(@$_);
                print $text;
                if ( \$_ != \$list[-1] ) {
                    if ($text ne '') {
                        # if not at end of list
                        print ",";
                    }
                }
            }
            print "]} ";      # end the JSON array
            return;            # end of JSON dump, return;
        }
        else {
            
            open STYLEFILE, "tuuber_chat.css";
            my $style=<STYLEFILE>;
            $style="* {
              margin: 0;
              padding: 0;
              color: white;
              background-color: black;
            }
            ul {
            
            }
            li {
              margin-bottom: 10px;
              list-style-type: none;
              padding: 5px;
              font-family: 'Raleway', sans-serif;
              line-height: normal;
            }
            .mine {
              background-color: #bfb5ff;
              color: #09380d;
              margin-left: 50px;
              margin-right: 100px;
              border-radius: 5px;
              list-style: inside url(\"https://fachtnaroe.net/images/iconMouth_bk.png\");
            }
            .theirs {
              background-color: #dce29c;
              color: #353941;
              margin-left: 100px;
              margin-right: 50px;
              border-radius: 5px;
              list-style: inside url(\"https://fachtnaroe.net/images/iconEar_bk.png\");
            }
            \@media screen and (max-width: 600px) {
              .mine {
                margin-left: 2px;
                margin-right: 20px;
                border-radius: 5px;
              }
              .theirs {
                margin-left: 20px;
                margin-right: 2px;
                border-radius: 5px;
              }
            } 
";
            my $iam=$cgi->param("iam");
            print "<!DOCTYPE html><html><head>
            <link href=\"https://fonts.googleapis.com/css?family=Raleway\" rel=\"stylesheet\"> 
            <style>$style</style></head><body><ul>";
            foreach (@list) {
                my $text = from_json( chatLine_GET(@$_) );
                print "<li class=\"";
                if ($iam eq $text->{'initiator_pID'}) {
                  print "mine";
                }
                else {
                  print "theirs";
                }
                print "\" >",$text->{'text'};
                
                if ( \$_ != \$list[-1] ) {
                    if ($text ne '') {
                        # if not at end of list
                        ;
                    }
                }
                print "</li>"
            }
            print "</ul></body></html>";
        }

        # do non-JSON dump
# 
#         $q = "SELECT persons.first, persons.family, chats.* FROM chats JOIN persons ON persons.pID=chats.respondent_pID WHERE chats.status LIKE 'open';";
#         $q = "SELECT * FROM chats;";
#         my $qh = $dbh->prepare($q);
#         $qh->execute();
#         my $file;
#         if ($showHtml) {
#             $file = "\n<table><tr>";
#             my @fieldNames = listTableFields('chats');
#             foreach (@fieldNames) {
#                 $file .= "<td><strong>$_</strong></td>";
#             }
#             $file .= "</tr>";
#         }
#         else {
#             $file = "";
#         }
#         while ( my @data = $qh->fetchrow_array() ) {
#             foreach (@data) {
#                 if ($showHtml) {
#                     $file .= "<td> $_ </td>";
#                 }
#                 else {
#                     $file .= $_ . "::";
#                 }
#             }
#             if ($showHtml) {
#                 $file .= "</tr>";
#             }
#             $file =~ s/::$//;    #remove final separator
#             $file .= "\n";
# 
#         }
#         if ($showHtml) {
#             $file .= "</table>\n";
#         }
#         if ( !$showHtml ) {
#             dbg($file);
#         }
#         print $file;
#         print "ENDS";
    }
    else {
    }
}

sub debugScreen {
    my ( $q, $result );

    # make debug output a JSON output
    print "{ debug : '";

    print <<__CSS1;
  <style> 
  table, td
  {
    border: 1px solid black;
    border-collapse: collapse;
  }
  table {
    width: 40%;
  }
  hr {
    border: 1px dotted black;
    width: 50%;
    float: left;
  }
  </style>
  <pre>
__CSS1
    $showHtml = 1;
    $action   = LIST;
    print "<pre><big><strong>Persons</strong></big></pre><hr>";
    persons();
    print "<br><pre><big><strong>Routes</strong></big></pre><hr>";
    routes();
    print "<br><pre><big><strong>Pools</strong></big> </pre><hr>";
    pools();
    dumpAccessRequests();
    print <<__CSS2;
  </pre>
__CSS2

    # end the 'JSON' request
    print "' }\n";
}

sub dumpAccessRequests {
    my @contents;

    # close output mode handle
    close FILE;

    # open input mode handle
    open FILE, "< $logfile";
    @contents = <FILE>;

    # close output mode and ...
    close FILE;

    # ... leave ready for outside of subroutine.
    open FILE, ">> $logfile";
    @contents = reverse @contents;
    print
"<pre><big><strong>Dump of access requests from debug file</strong>&nbsp;(reverse ordered)</big></pre><hr>";
    print "\n<table>";
    print
"<tr><td><strong>Line count</strong></td><td><strong>Line content</strong></td></tr>";
    my $c = scalar @contents;
    foreach (@contents) {
        $c--;
        print "<tr><td>$c</td><td>$_</td></tr>";
    }
    print "</table>";
    dbg("End of dump");
}

sub dbPrepare {
    require 'tuuber.conf.pl';
    $dbconn = "dbi:mysql:$db;$dbhost";
    $dbh = DBI->connect( $dbconn, $dbuser, $dbpasswd );
}

sub listTableFields {

    # Purpose: get list of fields in table
    # Expects: table name
    # Returns: array with field names
    my @fieldNames = ();
    my $table      = shift;
    my $query      = "DESCRIBE $table;";
    my $qh         = $dbh->prepare($query);    # prepare select query
    $qh->execute();                            # select now
    while ( my @one = $qh->fetchrow() ) {
        push @fieldNames, $one[0];
    }

    # Any field called 'password' will not be returned
    # This is better done on a per table basis, rather than globally as at here
    @fieldNames = grep { $_ ne 'password' } @fieldNames;
    return @fieldNames;
}

sub dbg {
    print FILE $_[0] . "\n";
}

sub deApostrophisise {
    my $returnValue = $_[0];
    $returnValue =~ s/'/\\'/g;
    return $returnValue;
}

sub reApostrophisise {
    my $returnValue = $_[0];
    $returnValue =~ s/\\'/'/g;
    return $returnValue;
}

sub loginCheck {
    my $userEmail = $cgi->param('email');
    my $userPass  = $cgi->param('password');
    my $query     = "SELECT pId, password from persons WHERE email = ?";
    my $qh = $dbh->prepare($query);    # prepare select query
    $qh->execute($userEmail);          # select now
    my ( $pID, $pass ) = $qh->fetchrow_array();
    my $sessionId = makeSessionID();
    my $json      = "{${quoteJSON}result${quoteJSON}:${quoteJSON}";

    if ( ( $pass eq $userPass ) && ( $userEmail ne '' ) && ( $userPass ne '' ) )
    {
        $json .= "OK";
    }
    else {
        $json .= "Error";
        $sessionId = '';
        $pID       = -1;
    }
    $json .= "${quoteJSON},${quoteJSON}sessionID${quoteJSON}:${quoteJSON}$sessionId${quoteJSON},";
    $json .= "${quoteJSON}pID${quoteJSON}:${quoteJSON}$pID${quoteJSON}";
    $json .= "}";
    return $json;
}

sub matches_GET {

    # usage:   person_GET(pID)
    # purpose: get details of one person, based on pID
    my $rID   = shift;
    #my $email = shift;
    my ( $q, $param );
    $q =
       "SELECT
        routes.pID, driver, rID, origin, destination, mon, tues, weds, thurs, fri, CONCAT(family, ', ', first) as realName, first, phone, email
        FROM routes
        JOIN persons ON (routes.pID=persons.pID)
        WHERE rID = ?;";
    my $qh = $dbh->prepare($q);
    $qh->execute($rID);
    my $result = $qh->fetchrow_hashref();
    my $text;
    if (!$showHtml) {
      $text = "{";
    }
    else {
      $text="<tr>";
    }
    my @fieldNames = ("pID", "driver", "rID", "origin", "destination", "mon", "tues", "weds", "thurs", "fri", "realName", "first", "phone", "email");
    foreach ( @fieldNames ) {
        $$result{$_} = deApostrophisise( $$result{$_} );
        if ($showHtml) {
          $text .= "<td style='border: 1px solid black'> $$result{$_} </td>";
        }
        else {
          $text .= "${quoteJSON}$_${quoteJSON}:${quoteJSON}" . $$result{$_} . "${quoteJSON},";
        }
    }
    if (!$showHtml) { 
      $text =~ s/,$//;    #remove final comma
      $text .= "}";
    }
    else {
      $text .= "</tr>";
    }
    return $text;
}

sub matches_list {

    # purpose: 
    # usage:   
    my $rID = shift;
    my $level= $cgi->param('level');
    my $q="
    SELECT * FROM routes WHERE rID = ?"; # first get the route for matching
    my $qh  = $dbh->prepare($q);
    $qh->execute($rID);
    my $searcher = $qh->fetchrow_hashref();
    my @list;
#      print "<br>>> ( $rID, $searcher->{origin}, $searcher->{mon}, $searcher->{tues}, $searcher->{weds}, $searcher->{thurs}, $searcher->{fri} ) <<<br>";
    # now, based on match level requested, get the matching routes
    if  (($level == 0) && (defined $level) )  { # seeking no match, but same town
      $q = "
      SELECT rID FROM routes
      WHERE ( (rID != ?)
      AND (origin LIKE ?)
      AND (mon NOT LIKE ?) AND (tues NOT LIKE ?) AND (weds NOT LIKE ?) AND (thurs NOT LIKE ?) AND (fri NOT LIKE ?) );";
      $qh  = $dbh->prepare($q);
      $qh->execute( $rID, $searcher->{origin}, $searcher->{mon}, $searcher->{tues}, $searcher->{weds}, $searcher->{thurs}, $searcher->{fri} );
    }
    elsif ( ($level == 1) || (!(defined $level)) )  { # the default, and level=1
      $q = "
      SELECT rID FROM routes
      WHERE ( (rID != ?)
      AND (origin LIKE ?)
      AND (mon LIKE ?) AND (tues LIKE ?) AND (weds LIKE ?) AND (thurs LIKE ?) AND (fri LIKE ?) );";
      $qh  = $dbh->prepare($q);
      $qh->execute( $rID, $searcher->{origin}, $searcher->{mon}, $searcher->{tues}, $searcher->{weds}, $searcher->{thurs}, $searcher->{fri} );
    }
    elsif ($level == 2) {
      $q = "
      SELECT rID FROM routes
      WHERE ( (rID != ?) 
      AND (origin LIKE ?)
      AND ( (mon LIKE ?) OR  (tues LIKE ?) OR (weds LIKE ?) OR (thurs LIKE ?) OR (fri LIKE ?) ) );";
      $qh  = $dbh->prepare($q);
      $qh->execute( $rID, $searcher->{origin}, $searcher->{mon}, $searcher->{tues}, $searcher->{weds}, $searcher->{thurs}, $searcher->{fri} );
    }
    elsif ($level == 3) {
      my $day=$cgi->param('day');
      $q = "
      SELECT rID FROM routes
      WHERE ( (rID != ?) 
      AND (origin LIKE ?)
      AND ($day LIKE 'Y')  );";
      $qh  = $dbh->prepare($q);
      $qh->execute( $rID, $searcher->{origin});
    }
    while( my $result = $qh->fetchrow() ) {
      push @list, $result;
    }
    return @list;
    
}

sub makeSessionID {
    return "a1b2c3d4";    # a temporary sessionId
}

sub patch_sql {
    my $sql = shift;

}

sub matches {
    my $rID    = $cgi->param('rID');
    my @list = matches_list( $rID );
#     print $rID;
#     foreach (@list) {print $_,"<br>"};
    if ( !$showHtml ) {    # if not showing html, do the JSON dump
      print " {${quoteJSON}matches${quoteJSON}:[";
    }
    else {
      print "<html><table style='border: 1px solid black; border-collapse: collapse'><tr>";
      my @fieldNames = ("pID", "driver", "rID", "origin", "destination", "mon", "tues", "weds", "thurs", "fri", "realName", "first", "phone", "email");
      foreach (@fieldNames) {
        print "<td style='border: 1px solid black'><strong>$_</strong></td>";
      }
      print "</tr>";
    }
    # get and print the data
    foreach (@list) {
      my $text= matches_GET($_);
      print $text;
      if (( \$_ != \$list[-1] ) && (!$showHtml) ) {
        #  if not at end of list
        if ($text ne '') {
            print ",";
        }
      }
    }
    # close off the table/json
    if ( !$showHtml ) {    # if not showing html, do the JSON dump
      print "]} ";      # end the JSON array
                  # end of JSON dump, return;
    }
    else {
      print "</table></html>";
    }
}

sub doPasswordChange {

    my $newPass  = $cgi->param('np');
    my $confirmPass  = $cgi->param('cp');
    my $pID = $cgi->param('pID');
    my $oldPass  = $cgi->param('op');
    if (($newPass ne $oldPass) || ($newPass eq '')) {}
    my $query = "SELECT password from persons WHERE pID = ?";
    my $qh = $dbh->prepare($query);    # prepare select query
    $qh->execute($pID);          # select now
    my ( $pass ) = $qh->fetchrow_array();
#     my $sessionId = makeSessionID();
    my $json      = "{${quoteJSON}result${quoteJSON}:${quoteJSON}";

    if ( ( $pass eq $oldPass ) && ( $pID ne '' ) && ( $oldPass ne '' ) 
      && ($newPass eq $confirmPass) )
    {
      $query = "UPDATE persons SET password=? WHERE pID = ?";
      $qh = $dbh->prepare($query);
      $qh->execute($newPass, $pID);
      $json .= "OK";
    }
    else {
        $json .= "Error";
        $sessionID = '';
    }
    $json .= "${quoteJSON}";
#     $json .= "${quoteJSON}pID${quoteJSON}:${quoteJSON}$pID${quoteJSON}";
    $json .= "}";
    return $json;

}

sub validSessionID {
  my $testID = shift;
  if ( $testID eq "" ) {
    return 0;
  }
  return 1;
}
# Version 0.5 Added code to return JSON array of people instead of text dump
# Version 0.8 Added chat code, passwording, converted passwording to cmd from entity
# Version 0.8.1 re-added pID my $q="SELECT pID, first, family, phone, email, spaces, driver FROM persons WHERE pID=?;";
# version 0.8.2 added real name to routes view as returned
# Version 0.8.4 enabled person selection by pID or email; returning of pID with successful login
# Version 0.8.5 edited to reflect remove of 'spaces' field, moving of 'driver' field; further edited to allow retrieval of routes by rID, pID, origin, destination, driver(Y/N) etc
# Version 0.8.6 allow combinations of route searches from 0.8.5
# version 0.8.7 return json of pID/sessionID when register is OK. (temp sessionID)
#               change loginCheck to return json text for printing
#               added JSON feedback on all of POST/PUT/DELETE
# version 0.8.8 added matches as entity, for selection of matching routes
#               fixed double comma ,, when empty json data
# 
# version 0.8.9 Added chat features, centered on chat status of init/open. First speaker uses the initiator_pID to 'init' the chat. Respondent 'open's in
#               reply. SUbsequent, each speaker uses initiator_pID as their own, uses 'open' as status on that line of chat.
# 2018-01-19:
# version 0.9  ${quoteJSON} introduced
# version 0.9.1 chats query returns full name of each person
# version 0.9.2 chats query retrusn both sides of conversation

# version 11+ Added password change CMD/PWC
#
# 2019-02-25:
# version 15: must have non-blank sessionID to procede beyond CMD/ACTION stage
#
# version 17: added support for 1 numerical 'day' field instead of using 5 day fields (m,t,w...)
