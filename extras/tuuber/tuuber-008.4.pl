#!/usr/bin/perl
use strict;

use constant progamVersion => "0.8.4";

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

# set-up the database connection. Configgy bits are in external config file
our ( $db, $dbconn, $dbhost, $dbuser, $dbpasswd, $dbh );
dbPrepare();

# read key CGI variables
my $action   = $cgi->param('action');     #$ENV{'REQUEST_METHOD'};
my $entity   = $cgi->param('entity');
my $cmd      = $cgi->param('cmd');
my $showHtml = $cgi->param('showHtml');

if ( $showHtml eq "" ) {
    my $showHtml = 0;
}

# the things/entities in the system that CGI variables nay refere to
use constant person => 'PERSON';
use constant route  => 'ROUTE';
use constant pool   => 'POOL';
use constant town   => 'TOWN';
use constant chat   => 'CHAT';

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

my $sessionId = $cgi->param('sessionId');

#===== Commands
if ( uc $cmd eq version ) {
    print "Version: " . progamVersion;
    exit;
}
elsif ( uc $cmd eq login ) {
    loginCheck();
}
elsif ( $entity eq "" ) {

#print "<span>No <b>entity</b> for operation specified, using <b>entity=debug</b></span>";
    $entity = debug;
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
else {
    dbg('type not matched');
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
"SELECT pID, first, family, phone, email, spaces, driver FROM persons WHERE pID=?;";
        $param = $pID;
    }
    else {
        $q =
"SELECT pID, first, family, phone, email, spaces, driver FROM persons WHERE email LIKE ?;";
        $param = $email;
    }
    my $qh = $dbh->prepare($q);
    $qh->execute($param);
    my $result = $qh->fetchrow_hashref();
    my $json   = "{ ";
    foreach ( keys %$result ) {
        $$result{$_} = deApostrophisise( $$result{$_} );
        $json .= "'$_' : '" . $$result{$_} . "', ";
    }
    $json =~ s/,\s$//;    #remove final comma
    $json .= " }\n";
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
    my $spaces = $cgi->param('spaces');
    my $driver = $cgi->param('driver');

    if ( uc $action eq POST ) {
        $q =
"INSERT INTO persons (first, family, phone, email, spaces, driver) VALUES (?, ?, ?, ?, ?, ?);";
        my $qh = $dbh->prepare($q);
        $result =
          $qh->execute( $first, $family, $phone, $email, $spaces, $driver );
    }
    elsif ( uc $action eq PUT ) {
        $q =
"UPDATE persons SET first=?, family=?, phone=?, email=?, spaces=?, driver=? WHERE pID=?;";
        my $qh = $dbh->prepare($q);
        $result =
          $qh->execute( $first, $family, $phone, $email, $spaces, $driver );
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
    }
    elsif ( uc $action eq LIST ) {
        if ( !$showHtml ) {    # if not showing html, do the JSON dump
            my @list = persons_ID_list();
            print " { 'persons' : [ ";
            foreach (@list) {
                print person_GET(@$_);
                if ( \$_ != \$list[-1] ) {

                    # if not at end of list
                    print ", ";
                }
            }
            print "] } ";      # end the JSON array
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
    my $rID = shift;

    #   my $q="SELECT * FROM routes WHERE rID=?;";
    my $q =
"SELECT routes.*, CONCAT(persons.first,' ', persons.family) AS personName FROM routes JOIN persons ON persons.pID=routes.pID WHERE rID=?;";
    my $qh = $dbh->prepare($q);
    $qh->execute($rID);
    my $result = $qh->fetchrow_hashref();
    my $json   = "{ ";
    foreach ( keys %$result ) {
        $$result{$_} = deApostrophisise( $$result{$_} );
        $json .= "'$_' : '" . $$result{$_} . "', ";
    }
    $json =~ s/,\s$//;    #remove final comma
    $json .= " }\n";
    return $json;
}

sub routes_ID_list {

    # purpose: get list of rID's (routes)
    # usage:   routes_ID_list()
    my $rID = shift;
    my $q   = "SELECT rID FROM routes;";
    my $qh  = $dbh->prepare($q);
    $qh->execute();
    my $result = $qh->fetchall_arrayref();

    # Cast address as array
    return @{$result};
}

sub routes {
    my ( $q, $result );

    # read the CGI input from the browser request
    my $rID    = $cgi->param('rID');
    my $origin = $cgi->param('origin');
    my $mon    = $cgi->param('mon');
    my $tues   = $cgi->param('tues');
    my $weds   = $cgi->param('weds');
    my $thurs  = $cgi->param('thurs');
    my $fri    = $cgi->param('fri');
    my $pID    = $cgi->param('pID');

    if ( uc $action eq POST ) {
        $q =
"INSERT INTO routes (origin, mon, tues, weds, thurs, fri, pID) VALUES (?, ?, ?, ?, ?, ?, ?);";
        my $qh = $dbh->prepare($q);
        $result =
          $qh->execute( $origin, $mon, $tues, $weds, $thurs, $fri, $pID );
    }
    elsif ( uc $action eq PUT ) {
        $q =
"UPDATE routes SET origin=?, mon=?, tues=?, weds=?, thurs=?, fri=?, pID=? WHERE rID=?;";
        my $qh = $dbh->prepare($q);
        $result =
          $qh->execute( $origin, $mon, $tues, $weds, $thurs, $fri, $pID, $rID );
    }
    elsif ( uc $action eq GET ) {
        my $json = route_GET($rID);
        print $json;
        print FILE $json;
    }
    elsif ( uc $action eq DELETE ) {
        $q = "DELETE FROM routes WHERE rID=?;";
        my $qh = $dbh->prepare($q);
        $result = $qh->execute($rID);
    }
    elsif ( uc $action eq LIST ) {
        if ( !$showHtml ) {    # if not showing html, do the JSON dump
            my @list = routes_ID_list();
            print " { 'routes' : [ ";
            foreach (@list) {
                print route_GET(@$_);
                if ( \$_ != \$list[-1] ) {

                    # if not at end of list
                    print ", ";
                }
            }
            print "] } ";      # end the JSON array
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
    my $json   = "{ ";
    foreach ( keys %$result ) {
        $$result{$_} = deApostrophisise( $$result{$_} );
        $json .= "'$_' : '" . $$result{$_} . "', ";
    }
    $json =~ s/,\s$//;    #remove final comma
    $json .= " }\n";
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
        print "{ 'pool_ID' : '$pool_ID' }";
    }
    elsif ( uc $action eq PUT ) {
        $q =
"UPDATE pools SET driver_pID=?, navigator_pID=?, rID=? WHERE pool_ID=?;";
        my $qh = $dbh->prepare($q);
        $result = $qh->execute( $driver_pID, $navigator_pID, $rID, $pool_ID );
    }
    elsif ( uc $action eq GET ) {
        $q =
          "SELECT driver_pID, navigator_pID, rID FROM pools WHERE pool_ID=?;";
        my $qh = $dbh->prepare($q);
        $qh->execute($pool_ID);
        $result = $qh->fetchrow_hashref();

        my $json = "{ ";
        foreach ( keys %$result ) {
            $$result{$_} = deApostrophisise( $$result{$_} );
            $json .= "'$_' : '" . $$result{$_} . "', ";
        }
        $json =~ s/,\s$//;    #remove final comma
        $json .= " }\n";
        print $json;
        print FILE $json;
    }
    elsif ( uc $action eq DELETE ) {
        $q = "DELETE FROM pools WHERE pool_ID=?;";
        my $qh = $dbh->prepare($q);
        $result = $qh->execute($pool_ID);
    }
    elsif ( uc $action eq LIST ) {

        if ( !$showHtml ) {    # if not showing html, do the JSON dump
            my @list = pools_ID_list();
            print " { 'pools' : [ ";
            foreach (@list) {
                print pool_GET(@$_);
                if ( \$_ != \$list[-1] ) {

                    # if not at end of list
                    print ", ";
                }
            }
            print "] } ";      # end the JSON array
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
    my $json   = "{ ";
    foreach ( keys %$result ) {
        $$result{$_} = deApostrophisise( $$result{$_} );
        $json .= "'$_' : '" . $$result{$_} . "', ";
    }
    $json =~ s/,\s$//;    #remove final comma and space
    $json .= " }\n";
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

        my $json = "{ ";
        foreach ( keys %$result ) {
            $$result{$_} = deApostrophisise( $$result{$_} );
            $json .= "'$_' : '" . $$result{$_} . "', ";
        }
        $json =~ s/,\s$//;    #remove final comma
        $json .= " }\n";
        print $json;
        print FILE $json;
    }
    if ( uc $action eq LIST ) {
        if ( !$showHtml ) {    # if not showing html, do the JSON dump
            my @list = towns_ID_list();
            print " { 'towns' : [ ";
            foreach (@list) {
                print town_GET(@$_);
                if ( \$_ != \$list[-1] ) {

                    # if not at end of list
                    print ", ";
                }
            }
            print "] } ";      # end the JSON array
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

sub chat_GET {

    # usage:
    # purpose: get details of one line of chat, based on pID
    my $line_ID = shift;
    my $q       = "SELECT * FROM chats WHERE line_ID=?;";
    my $qh      = $dbh->prepare($q);
    $qh->execute($line_ID);
    my $result = $qh->fetchrow_hashref();
    my $json   = "{ ";
    foreach ( keys %$result ) {
        $$result{$_} = deApostrophisise( $$result{$_} );
        $json .= "'$_' : '" . $$result{$_} . "', ";
    }
    $json =~ s/,\s$//;    #remove final comma
    $json .= " }\n";
    return $json;
}

sub chat_ID_list {

    # purpose: get list of chat line IDs
    # usage:   routes_ID_list()
    my $my_pID    = shift;
    my $other_pID = shift;
    my $q         = "
  SELECT line_ID FROM chats
  WHERE ( (my_pID=?) AND (other_pID=?) 
  OR (my_pID=?) AND (other_pID=?) )
  ORDER BY timestamp ASC;";
    my $qh = $dbh->prepare($q);
    $qh->execute( $my_pID, $other_pID, $other_pID, $my_pID, );
    my $result = $qh->fetchall_arrayref();

    # Cast address as array
    return @{$result};
}

sub chats {
    my ( $q, $result );
    my $line_ID   = $cgi->param('line_ID');
    my $my_pID    = $cgi->param('my_pID');
    my $other_pID = $cgi->param('other_pID');
    my $text      = $cgi->param('text');
    my $status    = $cgi->param('status');

    if ( uc $action eq POST ) {
        $q =
"INSERT INTO chats (my_pID, other_pID, text, status) VALUES (?, ?, ?, ?);";
        my $qh = $dbh->prepare($q);
        $result = $qh->execute( $my_pID, $other_pID, $text, $status );
        $line_ID = $dbh->{mysql_insertid};
        dbg("Created line with ID: $line_ID");
        print "{ 'line_ID' : '$line_ID' }";
    }
    elsif ( uc $action eq PUT ) {
        $q =
"UPDATE chats SET my_pID=?, other_pID=?, text=?, status=? WHERE line_ID=?;";
        my $qh = $dbh->prepare($q);
        $result = $qh->execute( $my_pID, $other_pID, $text, $status, $line_ID );
    }
    elsif ( uc $action eq GET ) {
        $q =
          "SELECT my_pID, other_pID, text, status FROM chats WHERE line_ID=?;";
        my $qh = $dbh->prepare($q);
        $qh->execute($line_ID);
        $result = $qh->fetchrow_hashref();

        my $json = "{ ";
        foreach ( keys %$result ) {
            $$result{$_} = deApostrophisise( $$result{$_} );
            $json .= "'$_' : '" . $$result{$_} . "', ";
        }
        $json =~ s/,\s$//;    #remove final comma
        $json .= " }\n";
        print $json;
        print FILE $json;
    }
    elsif ( uc $action eq DELETE ) {
        $q = "DELETE FROM chats WHERE line_ID=?;";
        my $qh = $dbh->prepare($q);
        $result = $qh->execute($line_ID);
    }
    elsif ( uc $action eq LIST ) {

        if ( !$showHtml ) {    # if not showing html, do the JSON dump
            my @list = chat_ID_list( $my_pID, $other_pID );
            print " { 'chat' : [ ";
            foreach (@list) {
                print chat_GET(@$_);
                if ( \$_ != \$list[-1] ) {

                    # if not at end of list
                    print ", ";
                }
            }
            print "] } ";      # end the JSON array
            return;            # end of JSON dump, return;
        }

        # do non-JSON dump

        $q = "SELECT * FROM chats;";
        my $qh = $dbh->prepare($q);
        $qh->execute();
        my $file;
        if ($showHtml) {
            $file = "\n<table><tr>";
            my @fieldNames = listTableFields('chats');
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

sub loginCheck {
    my $userEmail = $cgi->param('email');
    my $userPass  = $cgi->param('password');
    my $query     = "SELECT pId, password from persons WHERE email = ?";
    my $qh = $dbh->prepare($query);    # prepare select query
    $qh->execute($userEmail);          # select now
    my ( $pID, $pass ) = $qh->fetchrow_array();
    my $sessionId = 'a1b2c3d4';        # a temporary sessionId
    print "{ 'result' : '";

    if ( ( $pass eq $userPass ) && ( $userEmail ne '' ) && ( $userPass ne '' ) )
    {
        print "OK";
    }
    else {
        print "Error";
        $sessionId = '';
        $pID       = -1;
    }
    print "','sessionID' : '$sessionId',";
    print "'pID' : '$pID'";
    print "}";
}

# Version 0.5 Added code to return JSON array of people instead of text dump
# Version 0.8 Added chat code, passwording, converted passwording to cmd from entity
# Version 0.8.1 re-added pID my $q="SELECT pID, first, family, phone, email, spaces, driver FROM persons WHERE pID=?;";
# version 0.8.2 added real name to routes view as returned
# Version 0.8.4 enabled person selection by pID or email; returning of pID with successful login
