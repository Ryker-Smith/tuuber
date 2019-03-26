#!/usr/bin/perl
use strict;

use CGI;
use DBI;
use CGI::Carp ("fatalsToBrowser");
# are these used?:
use URI qw();
use URI::QueryParam qw();

print "Content-type: text/html\n\n";
my $cgi = CGI->new;
$cgi->header(-type => "application/json", -charset => "utf-8");
my $showHtml=0;
# $cgi->header(-type => "application/json", -charset => "utf-8");

# set-up the database connection
my ($db, $dbconn, $dbhost, $dbuser, $dbpasswd, $dbh);
dbPrepare();
# read key CGI variables
my $action=$cgi->param('action'); #$ENV{'REQUEST_METHOD'}; 
my $entity=$cgi->param('type');

# the things/entities in the system
use constant person => 'PERSON';
use constant route => 'ROUTE';
use constant pool => 'POOL';

# the actions to take with things/entities
use constant POST => 'POST';
use constant PUT => 'PUT';
use constant GET => 'GET';
use constant DELETE => 'DELETE';
use constant LIST => 'LIST';

# special actions
use constant login => 'LOGIN';
use constant register => 'REGISTER';
use constant debug => 'DEBUG';
my $logfile = "/home/public/tuber.dat";

my @data=$cgi->param;
open FILE, ">> $logfile";
use DateTime;
my $dt = DateTime->now;
my $time= join ' ', $dt->ymd, $dt->hms;
dbg("\n<b>-----New Query-----<b>\n" . $time . "\nFrom address: " . $ENV{REMOTE_ADDR} ."\nWith browser: " . $ENV{HTTP_USER_AGENT} . "\nParameters: ");
foreach (@data) {
  print FILE "($_=" . $cgi->param($_) . ") ";
}
print FILE "\n";

if ($entity eq "") {
	print "<span>No operation <b>type</b> specified, using <b>type=debug</b></span>";
	$entity=debug;
}
dbg($action);
dbg( $ENV{'QUERY_STRING'} );

if (uc $entity eq person) {
  persons();
}
elsif (uc $entity eq route) {
  routes();
}
elsif (uc $entity eq pool) {
  pools();
}
# elsif (uc $entity eq contact) {
#   contactDetails();
# }
elsif (uc $entity eq debug) {
  debugScreen();
}
elsif (uc $entity eq register) {
  registerPerson();
}
elsif (uc $entity eq login) {
  loginCheck();
}
else {
 dbg('type not matched');
}
dbg('-----END-----');
close FILE;

sub persons () {
  my ($q, $result);
  my $pID=$cgi->param('pID');
  my $first=$cgi->param('first');
  my $family=$cgi->param('family');
  my $phone=$cgi->param('phone');
  my $email=$cgi->param('email');
  my $spaces=$cgi->param('spaces');
  my $driver=$cgi->param('driver');
  
  if (uc $action eq POST) {
    $q="INSERT INTO persons (first, family, phone, email, spaces, driver) VALUES (?, ?, ?, ?, ?, ?);";
    my $qh=$dbh->prepare($q);
    $result=$qh->execute($first, $family, $phone, $email, $spaces, $driver);
  }
  elsif (uc $action eq PUT) {
    $q="UPDATE persons SET first=?, family=?, phone=?, email=?, spaces=?, driver=? WHERE pID=?;";
    my $qh=$dbh->prepare($q);
    $result=$qh->execute($first, $family, $phone, $email, $spaces, $driver);
  }
  elsif (uc $action eq GET) {
    $q="SELECT first, family, phone, email, spaces, driver FROM persons WHERE pID=?;";
    my $qh=$dbh->prepare($q);
    $qh->execute($pID);
    $result=$qh->fetchrow_hashref();
    my $json=  "{ ";
    foreach (keys %$result) {
      $$result{$_}=deApostrophisise($$result{$_});
      $json .= "'$_' : '" . $$result{$_} . "', ";
    }
    $json =~ s/,\s$//; #remove final comma
    $json .= " }\n";
    print $json;
    print FILE $json;
  }
  elsif (uc $action eq DELETE) {
    $q="DELETE FROM persons WHERE pID=?;";
    my $qh=$dbh->prepare($q);
    $result=$qh->execute($pID);
  }
  elsif (uc $action eq LIST) {
    my @columns=listTableFields('persons');
    @columns = grep { $_ ne 'password' } @columns;
    foreach (@columns) { $_ = $_ . ','; };
    chop ($columns[scalar @columns -1]);
    $q="SELECT @columns FROM persons;";
    my $qh=$dbh->prepare($q);
    $qh->execute();
    my $file;
    if ($showHtml) {
      $file="\n<table><tr>";
			my @fieldNames=listTableFields('persons');
			foreach (@fieldNames) {
			  $file .= "<td><strong>$_</strong></td>";
			}
			$file .= "</tr>";
    }
    else {
      $file=  "";
    }
    while (my @data=$qh->fetchrow_array()) {
      if ($showHtml) {
				$file .= "<tr>";
      }
      foreach (@data) {
				if ($showHtml) {
					$file .= "<td> $_ </td>" ;
				}
				else {
					$file .= $_ ."::" ;
				}
      }
      if ($showHtml) {
				$file .= "</tr>";
      }
      $file =~ s/::$//; #remove final separator
      $file .= "\n";
    }
    if ($showHtml) {
      $file .= "</table>\n";
    }
    if (!$showHtml) {
			dbg($file);
		}
    print $file;
  }
  else {
  }
}

sub routes {
  my ($q, $result);
  my $rID=$cgi->param('rID');
  my $origin=$cgi->param('origin');
  my $mon=$cgi->param('mon');
  my $tues=$cgi->param('tues');
  my $weds=$cgi->param('weds');
  my $thurs=$cgi->param('thurs');
  my $fri=$cgi->param('fri');
  my $pID=$cgi->param('pID');
  
  if (uc $action eq POST) {
    $q="INSERT INTO routes (origin, mon, tues, weds, thurs, fri, pID) VALUES (?, ?, ?, ?, ?, ?, ?);";
    my $qh=$dbh->prepare($q);
    $result=$qh->execute($origin, $mon, $tues, $weds, $thurs, $fri, $pID);
  }
  elsif (uc $action eq PUT) {
    $q="UPDATE routes SET origin=?, mon=?, tues=?, weds=?, thurs=?, fri=?, pID=? WHERE rID=?;";
    my $qh=$dbh->prepare($q);
    $result=$qh->execute($origin, $mon, $tues, $weds, $thurs, $fri, $pID, $rID);
  }
  elsif (uc $action eq GET) {
    $q="SELECT origin, mon, tues, weds, thurs, fri, pID FROM routes WHERE rID=?;";
    my $qh=$dbh->prepare($q);
    $qh->execute($rID);
    $result=$qh->fetchrow_hashref();
    my $json=  "{ ";
    foreach (keys %$result) {
      $$result{$_}=deApostrophisise($$result{$_});
      $json .= "'$_' : '" . $$result{$_} . "', ";
    }
    $json =~ s/,\s$//; #remove final comma
    $json .= " }\n";
    print $json;
    print FILE $json;
  }
  elsif (uc $action eq DELETE) {
    $q="DELETE FROM routes WHERE rID=?;";
    my $qh=$dbh->prepare($q);
    $result=$qh->execute($rID);
  }
  elsif (uc $action eq LIST) {
    $q="SELECT * FROM routes;";
    my $qh=$dbh->prepare($q);
    $qh->execute();
    my $file;
    if ($showHtml) {
      $file="\n<table><tr>";
			my @fieldNames=listTableFields('routes');
			foreach (@fieldNames) {
			  $file .= "<td><strong>$_</strong></td>";
			}
			$file .= "</tr>";
    }
    else {
      $file=  "";
    }
    while (my @data=$qh->fetchrow_array()) {
      if ($showHtml) {
				$file .= "<tr>";
      }
      foreach (@data) {
				if ($showHtml) {
					$file .= "<td> $_ </td>" ;
				}
				else {
					$file .= $_ ."::" ;
				}
      }
      if ($showHtml) {
				$file .= "</tr>";
      }
      $file =~ s/::$//; #remove final separator
      $file .= "\n";
    }
    if ($showHtml) {
      $file .= "</table>\n";
    }
    if (!$showHtml) {
			dbg($file);
		}
    print $file;
  }
  else {
  }
}

sub pool {
  my ($q, $result);
  my $jobNumber=$cgi->param('jobNumber');
  my $technicianNumber=$cgi->param('technicianNumber');
  my $jobDetails=$cgi->param('jobDetails');
  my $jobDone=$cgi->param('jobDone');
  my $jobReporter=$cgi->param('jobReporter');
  my $jobRoomNumber=$cgi->param('jobRoomNumber');
  my $jobDateReported=$cgi->param('jobDateReported');
  my $jobDateResolved=$cgi->param('jobDateResolved');
  my $jobTechnicianComments=$cgi->param('jobTechnicianComments');
  
  if (uc $action eq POST) {
    $q="INSERT INTO jobDetails (jobRoomNumber, technicianNumber, jobDetails, jobDone, jobReporter, jobNumber, jobTechnicianComments, jobDateReported, JobDateResolved) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    my $qh=$dbh->prepare($q);
    $result=$qh->execute($jobRoomNumber, $technicianNumber, $jobDetails, $jobDone, $jobReporter, $jobNumber, $jobTechnicianComments, $jobDateReported, $jobDateResolved);
    my $newJobNumber=$dbh->{mysql_insertid};
		dbg("Created jobDetails with ID: $newJobNumber");
		print "{ 'jobNumber' : '$newJobNumber' }";
  }
  elsif (uc $action eq PUT) {
    $q="UPDATE jobDetails SET jobRoomNumber=?, technicianNumber=?, jobDetails=?, jobDone=?, jobReporter=?, jobTechnicianComments=?, jobDateReported=?, jobDateResolved=? WHERE jobNumber=?;";
    my $qh=$dbh->prepare($q);
    $result=$qh->execute($jobRoomNumber, $technicianNumber, $jobDetails, $jobDone, $jobReporter, $jobNumber, $jobTechnicianComments, $jobDateReported, $jobDateResolved);
  }
  elsif (uc $action eq GET) {
    $q="SELECT jobRoomNumber, technicianNumber, jobDetails, jobDone, jobReporter, jobNumber, jobTechnicianComments, jobDateReported, jobDateResolved FROM jobDetails WHERE jobNumber=?;";
    my $qh=$dbh->prepare($q);
    $qh->execute($jobNumber);
    $result=$qh->fetchrow_hashref();
    
    my $json=  "{ ";
    foreach (keys %$result) {
      $$result{$_} = deApostrophisise($$result{$_});
      $json .= "'$_' : '" . $$result{$_} . "', ";
    }
    $json =~ s/,\s$//; #remove final comma
    $json .= " }\n";
    print $json;
    print FILE $json;
  }
  elsif (uc $action eq DELETE) {
    $q="DELETE FROM jobDetails WHERE jobNumber=?;";
    my $qh=$dbh->prepare($q);
    $result=$qh->execute($jobNumber);
  }
  elsif (uc $action eq LIST) {
    $q="SELECT jobNumber, jobRoomNumber, technicianNumber, jobDetails, jobDone, jobReporter, jobTechnicianComments, jobDateReported, jobDateResolved FROM jobDetails;";
    my $qh=$dbh->prepare($q);
    $qh->execute();
    my $file;
    if ($showHtml) {
      $file="\n<table><tr>";
			my @fieldNames=listTableFields('jobDetails');
			foreach (@fieldNames) {
			  $file .= "<td><strong>$_</strong></td>";
			}
			$file .= "</tr>";
    }
    else {
      $file=  "";
    }
    while (my @data=$qh->fetchrow_array()) {
      foreach (@data) {
			if ($showHtml) {
				$file .= "<td> $_ </td>" ;
			}
			else {
				$file .= $_ ."::" ;
			}
      }
			if ($showHtml) {
				$file .= "</tr>";
      }
      $file =~ s/::$//; #remove final separator
      $file .= "\n";
			
    }
		if ($showHtml) {
				$file .= "</table>\n";
		}
    if (!$showHtml) {
			dbg($file);
		}
    print $file;
  }
  else {
  }
}

sub debugScreen {
  my ($q, $result);
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
  $showHtml=1;
  $action=LIST;
  print "<pre><big><strong>People</strong></big></pre><hr>";
  persons();
  #print "<pre><big><strong>Customers</strong></big></pre><hr>";
  #customerDetails();
  print "<br><pre><big><strong>Routes   </strong></big></pre><hr>";
  routes();
  #print "<pre><big><strong>Contacts</strong></big> (ALT assignment)</pre><hr>";
  #contactDetails();
  dumpAccessRequests();
  print <<__CSS2;
  </pre>
__CSS2
}

sub dumpAccessRequests {
	my @contents;
	# close output mode handle
    close FILE;
	# open input mode handle
	open FILE, "< $logfile";
	@contents=<FILE>;
	# close output mode and ...
	close FILE;
	# ... leave ready for outside of subroutine.
	open FILE, ">> $logfile";
	@contents = reverse @contents;
	print "<pre><big><strong>Dump of access requests from debug file</strong>&nbsp;(reverse ordered)</big></pre><hr>";
	print "\n<table>";
	print "<tr><td><strong>Line count</strong></td><td><strong>Line content</strong></td></tr>";
	my $c=scalar @contents;
	foreach (@contents) {
		$c--;
	  print "<tr><td>$c</td><td>$_</td></tr>";
	}
	print "</table>";
}

sub dbPrepare {
  $db="tuuber";
  $dbhost="127.0.0.1";
  $dbuser="shroudycloudy";
  $dbpasswd="cloudymcshroudy";
  $dbconn="dbi:mysql:$db;$dbhost";
  $dbh = DBI->connect($dbconn, $dbuser, $dbpasswd);
}

sub listTableFields {
# Purpose: get list of fields in table
# Expects: table name
# Returns: array with field names
  my @fieldNames=();
  my $table=shift;
  my $query = "DESCRIBE $table;";
  my $qh = $dbh->prepare($query); # prepare select query
  $qh->execute(); # select now
  while (my @one= $qh->fetchrow()) {
    push @fieldNames, $one[0];
  }
  return @fieldNames;
}

sub dbg {
  print FILE $_[0] . "\n";
}

sub deApostrophisise {
  my $returnValue= $_[0];
  $returnValue =~ s/'/\\'/g;
  return $returnValue;
}

sub loginCheck {
	my $userEmail = $cgi->param('userEmail');
	my $userPass = $cgi->param('userPass');
	my $query="SELECT password from people WHERE email = ?";
	my $qh = $dbh->prepare($query); # prepare select query
	$qh->execute($userEmail); # select now
	my $pass=$qh->fetchrow();
	if (($pass eq $userPass) && ($userEmail != '') && ($userPass ne '')){
		print "PassOK";
		# dbg("user=$userPhone, pass=$userPass, savedPass=$pass, response=OK");
	}
	else {
		print "PasswordError";
		# dbg("user=$userPhone, pass=$userPass, savedPass=$pass, response=Error");
	}
}


