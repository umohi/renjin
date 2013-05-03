
test.CallReplacement <- function() {
	
	call <- quote(sin(x))
	call[[1]] <- "cos"
	assertThat(typeof(call), equalTo("language"))	
}

test.CallSubsetting <- function() {
	call <- quote(sin(x,y,z))
	call <- call[c(1L,2L)]
	assertThat(typeof(call), equalTo("language"))	
	
}

test.RemoveFunctionFromFunctionCall <- function() {
	call <- quote(sin(x))
	call[[1]] <- NULL
	assertThat(typeof(call), equalTo("pairlist"))
	assertThat(length(call), equalTo(1))
}


test.subsetDouble <- function() {
  x <- c(91,92,93) 
  assertThat(  x[1], equalTo( 91 ))
  assertThat(  x[2], equalTo( 92 ))
  assertThat(  x[3], equalTo( 93 ))
  assertThat(  x[4], is.na )
  assertThat(  x[0], identicalTo( double(0) ))
  assertThat(  x[NULL], identicalTo( double(0) ))
  assertThat(  x[3L], equalTo( 93 ))
}

test.subsetWithLogicals <- function() {
  x <- c(91,92,93)
  assertThat( x[c(TRUE,FALSE,TRUE)],equalTo( c(91,93)) )
}

test.listIndices <- function() {
  x <- list('a', 3, NULL) 
  
  assertThat( x[0],   identicalTo( list() ))
  assertThat( x[1],   identicalTo( list( "a" )))
  assertThat( x[99],  identicalTo( list( NULL )))
  assertThat( x[1:2], identicalTo( list( "a", 3 )))
  assertThat( x[2:5], identicalTo( list( 3, NULL, NULL, NULL ) ))
  assertThat( x[-3],  identicalTo( list( "a", 3 )))
}


test.emptyListNegativeIndices <- function() {
  x <- list()
  
  assertThat( x[4],   identicalTo(list(NULL)))
  assertThat( x[-1L], identicalTo(list()))
}


test.subsetDoubleMultipleIndices <- function() {
  x <- c(91,92,93)
  assertThat(  x[2:3] , identicalTo( c(92,93) ))
  assertThat(  x[3:5] , identicalTo( c(93, NA_real_, NA_real_) ))
}


test.stringSubset <- function() {
  x <- c('a','b','c') ;
  
  assertThat( x[0],   identicalTo( character(0) ))
  assertThat( x[1],   identicalTo( c("a") ))
  assertThat( x[99],  identicalTo( c( NA_character_ )))
  assertThat( x[1:2], identicalTo( c("a", "b") ))
  assertThat( x[2:5], identicalTo( c("b", "c", NA_character_, NA_character_ )))
  assertThat( x[-3] , identicalTo( c("a", "b")))
}


test.stringSubsetAssign <- function() {
  x <- c('a', 'b', 'c') 
  x[1] <- 'z' 
  
  assertThat(  x , identicalTo( c("z", "b", "c")))
}

test.assignNarrower <- function() {
  x <- c('a', 'b', 'c') 
  x[4] <- 36 
  
  assertThat(x , identicalTo( c("a", "b", "c", "36")))
}

test.assignWider <- function() {
  x <- c(1,2,3)
  x[2] <- c('foo')
  
  assertThat(  x , identicalTo( c("1", "foo", "3")))
}


test.negativeIndices <- function() {
  x <- c(91,92,93)  
  assertThat(  x[-1],         identicalTo( c(92,93) ))
  assertThat(  x[-1:-2],      identicalTo( c(93) ))
  assertThat(  x[c(-2,-241)], identicalTo( c(91,93) ))
  assertThat(  x[c(-1,0,0)],  identicalTo( c(92,93) ))
}

test.negativeIndicesOnMatrix <- function() {
  x<-1:8 
  dim(x) <- c(2,4)
  
  assertThat( x[,-4], equalTo( c(1,2,3,4,5,6) ))
}

#  (expected = EvalException.class)
#  test.mixedNegativeAndPos <- function() {
#     x <- c(91,92) ;
#     x[-1,4] ;
#  }

test.setDoubleSubset <- function() {
  x <- c(91, 92, 93) 
  x[1] <- 44 
  
  assertThat( x, equalTo( c(44,92,93 )))
}


test.logicalIndices <- function() {
  x <- c(21,22,23) 
  
  assertThat(  x[TRUE], identicalTo( c(21,22,23)))
  assertThat(  x[FALSE] , identicalTo( double(0) ))
  assertThat(  x[NA] , identicalTo( c(NA_real_, NA_real_, NA_real_) ))
  assertThat(  x[c(TRUE,FALSE,TRUE)] , identicalTo( c(21, 23) ))
  assertThat(  x[c(TRUE,FALSE)] , identicalTo( c(21, 23) ))
}

test.missingSubscript <- function() {
  x <- 41:43
  
  assertThat(  x[] , equalTo( c(41,42,43)) )
}

test.nrowsOnZeroWidthMatrix <- function() {
  m <- 1:12
  dim(m) <- c(3,4)
  m2 <- m[FALSE,,drop=FALSE]
  assertThat(dim(m2)[2], equalTo(4))
  l2 <- vector('logical',0)
  fin <- m2[!l2,,drop=FALSE];
  assertThat(dim(fin), equalTo(c(0,4)))
}


test.namedSubscripts <- function() {
  x <- c(a=3, b=4) 
  
  assertThat(  x['a'], identicalTo( c(a = 3) ))
}

test.namesPreservedCorrectly <- function() {
  x <- c(a=3, 99, b=4) 
  
  assertThat(  names(x[c(1,2,NA)]) , identicalTo( c( "a", "", NA_character_)))
}


test.setDoubleRange <- function() {
  x <- c(91, 92, 93) 
  x[1:2] <- c(81,82) 
  
  assertThat( x, identicalTo( c( 81, 82, 93 )))
}

test.setWithLogicalSubscripts <- function() {
  x <- 1:3 
  x[c(FALSE,TRUE,FALSE)] <- 99;
  
  assertThat( x, identicalTo( c(1,99,3)))
}

test.setWithLogicalSubscripts2 <- function() {
  x <- 1:4 
  x[c(FALSE,TRUE)] <- c(91,92)
  
  assertThat( x, identicalTo( c(1,91,3,92)))
}

test.setDoubleRangeMultiple <- function() {
  x <- c(91, 92, 93) 
  x[2:3] <- 63 
  
  assertThat( x, identicalTo( c( 91, 63, 63 )))
}

test.setDoubleRangeMultipleNewLength <- function() {
  x <- c(91, 92, 93) 
  x[2:5] <- 63 
  
  assertThat( x, identicalTo( c( 91, 63, 63, 63, 63 )) )
}


test.subsetOfPosAndZeroIndices <- function() {
  x<-c(91, 92, 93, 94, 95) 
  
  assertThat( x[c(1,0,1)], identicalTo(c(91, 91)))
}


test.setNoElements <- function() {
  x<- c(1,2,3) 
  x[FALSE]<-c()
  
  assertThat( x , equalTo(c(1,2,3)))
}


test.emptyLogicalVectorWithNoDimsIsAlwaysNull <- function() {
  assertThat( c()[1], is.null)
}


test.emptyVectorWithDimsIsNotNull <- function() {
  x<-TRUE[-1];
  dim(x) <- c(0,1)
  assertThat( x[1], is.na)
}


test.emptyDoubleVectorIsNotNull <- function() {
  select <- TRUE[-1]
  x <- 1[-1]
  assertThat( x[select], identicalTo(double(0)))
}


test.listElementByName <- function() {
  p <- list(x=33, y=44) ;
  
  assertThat( p$x, identicalTo( c(33) ));
}


test.setListElementByName <- function() {
  p <- list( x = 44 ) 
  assertThat(  names(p) , equalTo("x" ))
  
  p$x <- 88 ;
  
  assertThat(  p$x , equalTo( 88 ))
}


test.replaceListElementWithList <- function() {
  restarts <- list( list(name='foo'), list(name='zig'), list(name='zag') ) 
  
  assertThat( restarts[[2]]$name , equalTo(c("zig")))
  
  name <- 'bar'
  i <- 2
  restarts[[i]]$name <- name 
  
  assertThat( restarts[[2]]$name , equalTo(c("bar")))
  
}

# (expected = EvalException.class)
# test.replaceElementInAtomicVectorWithNullFails <- function() {
#   x <- c(1,2,3) 
#   x[[1]] <- NULL 
# }

# x[1] <- NULL and x[1] <- c() both remove the first element
# x[1] <- list() sets the first element to an empty list
# x[[1]] <- list() 

#  (expected = EvalException.class)
#  test.replaceSingleElementInListWithEmptyListThrows <- function() {
#    x<- c(1,2,3) 
#     x[[1]] <- list() 
# }


test.replaceSingleElementInListWithNullRemovesElement <- function() {
  x <- list(1,2,3)
  x[[1]] <- NULL 
  
  assertThat( x, identicalTo(list(2,3)))
}



test.replaceElementsInListWithNullRemovesElement <- function() {
  x <- list(1,2,3) 
  x[1:2] <- NULL 
  
  assertThat( x, identicalTo(list(3)))
}


test.replaceElementInListWithNullRemovesElement <- function() {
  x <- list(1,2,3) 
  x[1] <- NULL 
  
  assertThat( x, identicalTo(list(2,3)))
}

test.setNewListElementByName <- function() {
  p <- list( x = 22, y = 33 ) 
  p$z <- 44 
  
  assertThat(  p$x , equalTo( 22 ))
  assertThat(  p$y , equalTo( 33 ))
  assertThat(  p$z , equalTo( 44 ))
}


test.partialListMatch <- function() {
  x <- list(alligator=33, aardvark=44) 
  
  assertThat( x$a, is.null)
  assertThat( x$all, equalTo( 33 ))
}


test.exactMatch <- function() {
  x <- list(a=1, aa=2) 
  
  assertThat(  x$a , equalTo( 1))
}

#   
#   test.pairListPartial <- function() {
# 
#     PairList list = PairList.Node.newBuilder()
#         .add(as.symbol("alligator"), c(1))
#         .add(as.symbol("aardvark"), c(3))
#         .build();
# 
#     SEXP result = Subsetting.getElementByName(list, Symbol.get("all"));
#     assertThat(result, equalTo((SEXP)c(1)));
#   }
# 
#   (expected = EvalException.class)
#   test.listIndexOutOfBounds <- function() {
#      x <- list(1,2) ;
#      x[[3]] ;
#   }


test.assignListToListElement <- function() {
  x<- list() 
  x[['foo']] <- list(a=1,b=2,c=3)
  
  assertThat(  x[['foo']] , identicalTo(list(a=1,b=2,c=3)))
}

test.indexOnNull <- function() {
  x<- NULL 
  assertThat( x[[1]], is.null)
}


test.buildListByName <- function() {
  x<-list()
  x[['a']] <- 1
  x[['b']] <- 2
  x[['c']] <- 3
  
  assertThat(x, identicalTo(list(a=1,b=2,c=3)))    
}


test.columnIndex <- function() {
  x <- 1:8 
  dim(x) <- c(4,2) 
  
  assertThat( x[,2], equalTo( c(5,6,7, 8) ))
  assertThat( dim(x[,2]), is.null)
  assertThat( dim(x[,2,drop=TRUE]), is.null)
  assertThat( dim(x[,2,drop=FALSE]), equalTo( c(4, 1) ))
}

test.rows <- function() {
  x <- 1:8 
  dim(x) <- c(4,2) 
  
  assertThat( x[3:4,], equalTo( c(3,4,7,8) ))
  assertThat( dim(x[3:4,]), equalTo( c(2,2) ))
}

test.byNamedCol <- function() {
  x <- rbind(c(a=1,b=2) )
  
  assertThat(  x[,'b'] , equalTo( c(2) ))
}

test.arrayDimsCorrectlyPreserved <- function() {
  x<- 1:8 
  dim(x) <- 8
  
  assertThat(  dim(x[1:4]) , equalTo( 4 ))
  assertThat(  dim(x[1]) , is.null)
  assertThat(  dim(x[1,drop=FALSE]) , equalTo( 1 ))
}

test.matrixDimsPreserved <- function() {
  x<-1:4 
  dim(x) <- c(2,2) 
  x[1,1] <- 9
  
  assertThat( dim(x), equalTo( c(2,2)))
  
}


test.matrixDimsPreserved2 <- function() {
  x<-.Internal(rep.int(0,29*29)) 
  dim(x) <- c(29,29) 
  
  y<-c(134L,33L,2L,46L)
  dim(y) <- c(2,2) 
  
  x[c(1,2), c(3,4)] <- y
  
  assertThat( dim(x), equalTo( c(29, 29)))
}


test.matrices <- function() {
  x<-1:12
  dim(x) <- c(3,4)
  
  assertThat( x[2,3], equalTo(8))
  assertThat( x[1,NULL], identicalTo(integer(0)) )
  assertThat( dim(x[1,NULL]), is.null)
}



test.subscriptsOnNull <- function() {
  x <- NULL 
  
  assertThat(  x[1] , is.null)
  assertThat(  x[c(TRUE,FALSE)] , is.null)
  assertThat(  x[c(1,2,3)] , is.null)
  assertThat(  x[-1] , is.null)
  assertThat(  x[] , is.null)
}


test.integerIndex <- function() {
  x<- FALSE 
  assertThat(  x[1L] , equalTo( c(FALSE)))
}


test.replaceListItem <- function() {
  x<- list(91, 'foo', NULL) 
  x[[3]] <- 41 
  
  assertThat( x, identicalTo( list(91, "foo", 41)) )
}

test.replaceMatrixElements <- function() {
  
  x <- c(40, 1, 87, 6, 2, 8, 0, 28, 0, 43)
  dim(x) <- c(5,2)
  
  A <- .Internal(rep.int(0,9*9))
  dim(A) <- c(9,9)
  
  A[5:9,1:2] <- x
  
  assertThat( A[5,1], equalTo(40))
  assertThat( A[5,2], equalTo(8))
}


test.replaceVectorItemWithWidening <- function() {
  x<- c(91,92) 
  x[[2]] <- 'foo' 
  
  assertThat( x, equalTo( c("91", "foo")) )
}


test.addNewListItemViaReplaceSingleItem <- function() {
  x<-list() 
  x[[1]] <- 'foo' 
  
  assertThat( x, identicalTo( list("foo" )))
}


test.replaceSingleElementInEnvironment <- function() {
  x <- globalenv()
  x[['foo']] <- 42
  
  assertThat(foo, equalTo(42))
}


test.addNewListItemByNameViaReplaceSingleItem <- function() {
  x<- list() 
  x[['foo']] <- 'bar'
  
  assertThat( x, identicalTo( list(foo = "bar")))
}

test.replaceColumn <- function() {
  a<-1:30;
  dim(a) <- c(10,3) ;
  T<-TRUE;
  a[ c(T,T,T,T,T,T,T,T,T,T), 3] <- 51:60 ;
  
  assertThat( length(a), equalTo(30));
}


test.pairListConverted <- function() {
  p <- as.vector(list(a=1, b=2, 3, 4), 'pairlist')
  assertThat( p[1:2], identicalTo(list(a=1,b=2)))
  assertThat( names(p[TRUE]), identicalTo(c("a", "b", "", "")))
  assertThat( p[['b']], identicalTo(2))
  
  p[[1]]<-99;
  assertThat( typeof(p), equalTo("pairlist"))
  assertThat( p$a, equalTo(99))
  
}


test.pairListSingleByName <- function() {
  p <- .Internal(as.vector(list(hello=1, b=2, 3, 4), 'pairlist'));
  
  assertThat( p[['h']], is.null)
  assertThat( p[['hello']], equalTo(1))
  assertThat( p[['h', exact=FALSE]], equalTo(1))
}


test.pairListReplaceByName <- function() {
  x <- as.vector(list(a=1, z=4), 'pairlist')
  x$b<-2
  x$a<-4
  x$z<-NULL
  
  assertThat( length(x), equalTo(2))
  assertThat( x$a, equalTo(4))
  assertThat( x$b, equalTo(2))
  
}


test.emptyLogicalIndex <- function() {
  x <- 1:12 
  dim(x) <- 3:4 
  y <- x[ c(), , drop=FALSE] 
  assertThat(typeof(y), equalTo("integer"))
  assertThat(dim(y), equalTo(c(0, 4)))
  
}

test.pairListElipses <- function() {
  x <- as.vector(list(a=1, z=4), 'pairlist')
  x$... <- 4
  assertThat( x$..., equalTo(4))
}


test.indexingCharacter <- function() {
  vars <- quote(list(weighta))
  assertThat( vars[[2]], identicalTo(as.symbol("weighta")))
}


test.assignSymbol <- function() {
  k<-list(1,2,3)
  k[[2]]<-quote(foo)
}

test.coordinateMatrices <- function() {
  
  x<-1:12
  dim(x) <- c(3,4) 
  
  # define a matrix with coordinates in rows
  # 1 3
  # 3 4
  coords <- c(1,3,3,4)
  dim(coords) <- c(2,2)
  
  assertThat(x[coords], equalTo(c(7, 12)))
  
  # logical matrices should NEVER be treated as coordinate
  # matrices, regardless of their dimension
  assertThat(x[coords == 1], equalTo(c(1,5,9)))
}


test.environmentSymbol <- function() {
  .testEnv<-new.env();
  assign("key",1,.testEnv)
  assign("value","foo",.testEnv)
  assertThat(if(.testEnv[["key"]]==1) TRUE else FALSE, identicalTo(TRUE))
  assertThat(if(.testEnv[["value"]]=="foo") TRUE else FALSE,identicalTo(TRUE))
}

test.emptyLogical <- function() {
  x <- 1:10
  assertThat(x[logical(0)], identicalTo(integer(0)))
}


test.dimNamesToNamesWhenDrop <- function() {
  x <- 1:12
  dim(x) <- c(3,4)
  dimnames(x) <- list(c('A','B','C'), NULL)
  
  y <- x[,1L]
  assertThat(names(y), equalTo(c("A","B","C")))
  
}

#   (expected = EvalException.class)
#   test.absetDimNamesThrowsEvalException <- function() {
#     x <- c(1,2,3,4);
#     dim(x) <- c(1,4);
#     dimnames(x) <- list(NULL, c('a','b','c','d'));
#     y <- x[,c('a','b', 'x')];
#     assertThat(y, equalTo(c(1,2)));
# 
#   }


test.subsetLang <- function() {
  formula <- y ~ x
  assertThat(print(formula[[2]]), identicalTo(as.symbol("y")))
}


test.matrixColumn <- function() {
  `[.foo` <- function(x,i,j) { class(x) <- 'matrix'; NextMethod('[') }
  
  x <- as.double(1:12)
  dim(x) <- c(6,2)
  class(x) <- 'foo'
  
  assertThat(x[,2], equalTo(c(7,8,9,10,11,12)))
}
