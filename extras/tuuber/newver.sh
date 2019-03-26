#!/bin/sh
tuuber="tuuber-0"
old=$tuuber$1.pl
new=$[$1+1]
new=$tuuber$new.pl
tuuber="tuuber"

echo Was $old
echo Now making $new
cp $old $new
ln -sf $new $tuuber-test.pl
ln -sf $old $tuuber.pl
