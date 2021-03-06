#  File src/library/base/R/paste.R
#  Part of the R package, http://www.R-project.org
#
#  This program is free software; you can redistribute it and/or modify
#  it under the terms of the GNU General Public License as published by
#  the Free Software Foundation; either version 2 of the License, or
#  (at your option) any later version.
#
#  This program is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#  GNU General Public License for more details.
#
#  A copy of the GNU General Public License is available at
#  http://www.r-project.org/Licenses/

paste <- function (..., sep = " ", collapse = NULL)
    .Internal(paste(list(...), sep, collapse))

# Add a pure R version (http://stackoverflow.com/a/21230815) of the paste0
# function which was introduced in R-2.15.0, see the release notes at
# http://cran.r-project.org/src/base/NEWS.2
paste0 <- function(..., collapse = NULL) {
    paste(..., sep = "", collapse = collapse)
}

##=== Could we consider a  .Primitive  *fast*
##  paste2 <- function(x,y)  paste(x,y, sep='')

##=== Could we extend  paste(.) to (optionally) accept a
##    2-vector for collapse ?	 With the following functionality

##- paste.extra <- function(r, collapse=c(", "," and ")) {
##-	    n <- length(r)
##-	    if(n <= 1) paste(r)
##-	    else
##-	      paste(paste(r[-n],collapse=collapse[1L]),
##-		    r[n], sep=collapse[min(2,length(collapse))])
##- }
