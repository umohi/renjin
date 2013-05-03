
test.unaryFunction <- function() {
  result <- sqrt(4);
  
  assertThat(result, equalTo(2));
}

test.vectorizedSqrt <- function() {
  result <- sqrt(c(1,4,9))[2];
  assertThat(result,equalTo(2));
}


test.ifStatement <- function() {
  assertThat(if(TRUE) 1, equalTo(1));
}

test.ifStatementWithArgsToBeEvaluated <- function() {
  x<-1
  assertThat(if(x) 9, equalTo(9));
}


test.ifElseStatement <- function() {
  assertThat(if(TRUE) 1 else 2, equalTo(1));
}


test.ifElseFalseStatement <- function() {
  assertThat(if(FALSE) 1 else 2, equalTo(2));
}

test.ifWithNA <- function() {
  assertThat(if(NA) 1, throws.error)
}

test.braces <- function() {
  assertThat({1; 2}, equalTo(2));
}


test.emptyBraces <- function() {
  assertThat({}, is.null);
}

test.assign <- function() {
  assertThat(x<-2, equalTo(c(2)));
  assertThat(x, equalTo(c(2)));
}

test.oldAssign <- function() {
  assertThat((x=2), equalTo(2));
  assertThat(x, equalTo(2));
}

test.assignPrecedence <- function() {
  x<-1;
  f<-function(z) { if(z!=1) stop('expected z==1'); 42 } ;
  x<-f(x);
}


# test.assignIsSilent <- function() {
#   x<-1;
#   assertThat(topLevelContext.getSession().isInvisible(), equalTo(TRUE));
# }
# 
# 
# test.invisibleFlagIsReset <- function() {
#   x<-1;
#   x;
#   assertThat(topLevelContext.getSession().isInvisible(), equalTo(FALSE));
# }


test.assignSym <- function() {
  x<-1
  y<-x
  assertThat(y, equalTo(1))
}


test.whileLoop <- function() {
  x<-TRUE
  while(x) { 
    x<-FALSE 
  }
  
  assertThat(x, equalTo(FALSE))
}


test.whileLoopWithBreak <- function() {
  x<-TRUE
  while(x) { break; x<-FALSE }
  
  assertThat(x, equalTo(TRUE))
}


test.repeatLoop <- function() {
  y<-0
  repeat { 
    y <- y + 1
    if(y > 5) break 
  }
  
  assertThat( y, equalTo(6));
}

test.whileLoopWithNext <- function() {
  x<-1
  y<-0
  while(x<5) {  
    x<-x+1
    if(x==3) next
    y<-y+1 
  }
  
  assertThat(y, equalTo( 3 ))
}

test.evalOrderArgs <- function() {
  
  f <- function(x = (z + 1)) { 
    mx <- missing(x)
    z <- 1
    x
  }
  
  assertThat(f(), equalTo(2));
}

test.simplestForStatement <- function() {
  for( x in 99 ) { y <- x} 
  
  assertThat(x, equalTo(99))
  assertThat(y, equalTo(99))
}

test.forOverList <- function() {
  alist <- c('a','b','c');
  for(item in alist) { y<-item } 
  
  assertThat(y, equalTo("c"))
}

test.function <- function() {
  f <- function(x) { x }
  assertThat(f(4), equalTo(4))
}

test.functionWithMissing <- function() {
  f <- function(x) { missing(x) }
  assertThat(f(), equalTo(TRUE))
  assertThat(f(1), equalTo(FALSE))
}

test.missingArgPropogates <- function() {
  f <- function(x) missing(x) 
  g <- function(x) f(x) 
  h <- function(x) g(x)
  assertThat(g(), equalTo(TRUE))
  assertThat(h(), equalTo(TRUE))
}

test.missingWithDefaultArg <- function() {
  f<-function(x=1) missing(x) 
  
  assertThat( f(), equalTo( TRUE))
}

test.missingWithNullDefaultAndGenerics <- function() {
  f.default <- function(formula, data=NULL) 
    missing(data) 
  
  f <- function(formula, ...) UseMethod('f')
  
  data <- 88
  assertThat( f(1, data=data), equalTo(FALSE)) 
}

test.missingWithDefaultArgPart2 <- function() {
  y <- 4
  f<-function(x=1) missing(x) 
  
  assertThat( f(y), equalTo(FALSE) )
}

test.functionWithZeroArgs <- function() {
  f <- function() { 1 } 
  assertThat(f(), equalTo(1))
}

test.onExit <- function() {
  
  f<-function() { 
    on.exit( .Internal(eval(quote(launchMissiles<-42), globalenv(), NULL))) 
  }
  f() 
  
  assertThat(  launchMissiles , equalTo( 42 ) )
}

test.onExitCorrectEnvironment <- function() {
  f<- function() { 
    tutty.fruity <- 3
    on.exit(tutty.fruity+1)
  }
  f() 
}

test.globalAssign <- function() {
  
  myf <- function(x) {
    innerf <- function(x) .Internal(assign("Global.res", x^2, globalenv(), FALSE))
    innerf(x+1)
  }
  myf(3)
  
  assertThat( Global.res, equalTo( 16 ))
}


test.complexAssignment <- function() {
  x <- list(a = 1)
  x$a <- 3
  
  assertThat( x$a, equalTo( 3 ))
}


test.complexReassignment <- function() {
  x <- list(a = 1)
  f <- function() x$a <<- 3
  f()
  
  assertThat( x$a, equalTo( 3 ))
}


test.complexAssignmentWithClass <- function() {
  x<- list(a = 1)
  class(x$a) <- 'foo'
  
  assertThat(  x$a , equalTo( 1 ))
  assertThat(  class(x$a) , equalTo( "foo" ))
}

test.complexAssignmentWithSubset <- function() {
  x <- list( a = c(91,92,93) )
  x$a[3] <- 42
}

test.complexAssignmentWithElipses <- function() {
  f<-function(x,f,drop=FALSE) x ;
  `f<-` <- function(x,f,drop=FALSE,...,value) { 
      .Internal(assign('d', drop, globalenv(), FALSE))
      x 
  } 
  y <- 3
  
  f(y,1:10) <- 4
  
  assertThat(d, equalTo(FALSE))
}

test.chainedComplexAssignment <- function() {
  x <- y <- z <- 1
  
  assertThat( x, equalTo(1))
  assertThat( y, equalTo(1))
  assertThat( z, equalTo(1))
  
  class(x) <- class(y) <- class(z) <- 'foo';
  
  assertThat( class(x), equalTo("foo"))
  assertThat( class(y), equalTo("foo"))
  assertThat( class(z), equalTo("foo"))
}


test.functionLookup <- function() {
  
  f<-function(x) x
  g<-function() { f<-3; f(f); }
  
  assertThat(  g() , equalTo(3))
}

test.dotDotDotToPrimitive <- function() {
  f<-function(...) sqrt(...)
  assertThat( f(4), equalTo(2))
}

test.substitute <- function() {
  f1 <- function(x, y = x)             { x <- x + 1; y }   
  s1 <- function(x, y = substitute(x)) { x <- x + 1; y }   
  s2 <- function(x, y) { if(missing(y)) y <- substitute(x); x <- x + 1; y } 
  
  a <- 10  
  
  assertThat(  f1(a), equalTo( c(11) ) )
  assertThat(  s1(a), equalTo( c(11) ) )
  assertThat(  s2(a), equalTo( as.symbol("a") ))
}

test.substituteWithList <- function() {
  assertThat(  substitute(x, list(x=42)) , equalTo(42))
}

test.substituteDotDot <- function() {
  f <- function(...) substitute(list(...)) 
  x <- f(a,b)
  assertThat( typeof(x), equalTo("language") )
  #assertThat( f(a,b), identicalTo( call("list", as.symbol("a"), as.symbol("b")) ) )
}

test.listFromArgs <- function() {
  f<- function(...) list(...) 
  
  assertThat( f(1,2,3), identicalTo(list(1,2,3)))
}

test.returnInPromises <- function() {
  x <- 0
  f <- function() { 
    g <- function(expr) {  x<<-1; expr }
    g(return(42)) 
    return(x) 
  }
  
  assertThat( f(), equalTo(42))
}

test.quoteSymbol <- function() {
  x <- quote(y)
  assertThat( x, identicalTo(quote(y)) )
}

test.symbolToCharacter <- function() {
  assertThat(  as.character(quote(x)) , equalTo( "x" ))
}

test.doSwitch <- function() {
  
  assertThat( switch('z', alligator=4,aardvark=2, 44), equalTo( 44 ))
  assertThat( switch('a', alligator=4,aardvark=2, 44), equalTo( 44 ))
  assertThat( switch('a', alligator=4,aardvark=2), is.null)
  assertThat( switch('alligator', alligator=4,aardvark=2), equalTo(4))
  assertThat( switch('all', alligator=4,aardvark=2), is.null)
  assertThat( switch('all'),  is.null)
  
  assertThat( switch(1, 'first', 'second'), equalTo( "first" ))
  assertThat( switch(2, 'first', 'second'), equalTo( "second" ))
  assertThat( switch(99, 'first', 'second'), is.null )
  assertThat( switch(4), is.null)
  
  assertThat( switch('a', a=,b=,c=3) , equalTo(3))
  assertThat( switch(NA_character_, a=1,b=2), is.null)
}

# 
# test.useMethod <- function() {
#   fry <- function(what, howlong) UseMethod('fry') 
#   fry.default <- function(what, howlong) list(desc='fried stuff',what=what,howlong=howlong) 
#   fry.numeric <- function(what, howlong) list(desc='fried numbers',number=what,howlong=howlong)
#   
#   x<-33
#   class(x) <- 'foo'
#   
#   assertThat( fry(1,5), identicalTo(list(desc='fried numbers', number=1, howlong=5)) ) 
#   assertThat( fry(x,15), identicalTo(list(desc='fried stuff', what=33, howlong=15)) ) 
#   
#   cook <- function() { eggs<-6; fry(eggs, 5) }
#   
#   assertThat( cook(), identicalTo( list(desc='fried numbers', what=6, howlong=5) ))
# }

#(expected=EvalException.class)
#test.useMethodFailsOnMissingMethod <- function() {
#  f <- function(x) UseMethod('f');
#  f.foo <- function(x) 'matrix' ;
#  f(9);
#}

test.useMethodDispatchesToMatrices <- function() {
  f <- function(x) UseMethod('f')
  f.matrix <- function(x) 'matrix' 

  m <- 1:12
  dim(m) <- c(3,4)
  
  assertThat(f(m), equalTo("matrix"))
}

test.useMethodDispatchesToDoubleThenNumeric <- function() {
  f <- function(x) UseMethod('f')
  f.numeric <- function(x) 'numeric' 
  f.double <- function(x) 'double' 

  assertThat(f(9), equalTo("double"))
}

test.nargs <- function() {
  test <- function(a, b = 3, ...) {nargs()}
  
  assertThat( test(), equalTo( c(0)) );
  assertThat( test(clicketyclack), equalTo( 1 ))
  assertThat( test(c1, a2, rr3), equalTo( 3 ))
}


test.delayedAssign <- function() {
    
  delayedAssign('x', f(y)) 
  y<-3
  f<-function(x) x^2 
  
  assertThat( x, equalTo(9) )
}


test.evalWithPairList <- function() {
  params <- list(a=1,b=99)
  c<-25
  assertThat(eval(quote((a+b)/c), params), equalTo(4))
}

test.rhsIsEvaledOnlyOnce <- function() {
  assign('.evalCount', value=0, envir=globalenv())
  
  onlyonce <- function() { 
    if(globalenv()$.evalCount != 0) 
        stop("evaled twice")
    assign('once', value=1, envir=globalenv())
    16 
  }
  k <- list(1,2,3)
  k[[2]] <- onlyonce()

  assertThat( k, identicalTo(list(1,16,3)))
}


test.intermediateAssignmentTargetsAreNotEvaled <- function() {
  x<- quote(shouldNotBeEvaled()) 
  attr(x, 'foo') <- 'bar' 
  environment(x) <- globalenv() 
  class(x) <- 'foo' 
}


test.matchCall <- function() {

  f<-function(a,b) match.call()
  matched <- f(b=1,a=2)
  
  assertThat(matched$a, equalTo(2))
  assertThat(matched$b, equalTo(1))
}

test.matchCallWithMissingArgs <- function() {

  f<-function(a,b) match.call();
  matched <- f(b=1);
  
  assertThat(length(matched), equalTo(2))
}

test.matchCallDotsNotExpanded <- function() {

  f<-function(expand.dots,...) 
    match.call(expand.dots=expand.dots)
  
  # try without dots expanded
  matched <- f(expand.dots=FALSE, 1,2,3)
  
  assertThat(as.list(matched$...), identicalTo(list(1,2,3)))
  
  # now with dots expanded
  matched <- f(expand.dots=TRUE, 44, 55, 90, 50)
  
  assertThat(matched$..., is.null)
  assertThat(length(matched), equalTo(6))
}


#test.primitive <- function() {
# f <- .Primitive('if');
#  assertThat(typeof(), instanceOf(IfFunction.class));
#} 

test.lapplyWithFunctionCalls <- function() {
  g<-function(x) .Internal(as.vector(x, 'list'))
  f<-function(x) g(substitute(x))
  z<-f(~(0+births))
  
  assertThat(typeof(z[[2]]), equalTo("language"))
}

test.nextMethodWithMissing <- function() {

  `[.foo` <- function(x, ..., drop = explode()) 
    NextMethod() 
  x<-1
  class(x) <- 'foo';
  x[1]
}

test.nextMethodClosure <- function() {
  g.default <- function(x, b = 42) b 
  g.foo <- function(x, b = 22) NextMethod() 
  g <- function(x,b = 16) UseMethod('g') 
  x<-1
  class(x) <- 'foo'
  assertThat(g(x), equalTo(c(42)))
}


test.nextMethodArgReorder <- function() {

  g.default <- function(b,a) b 
  g.foo <- function(a,b) NextMethod() 
  g <- function(a,b) UseMethod('g') 
  x <- 1
  class(x) <- 'foo'
  assertThat(g(41,42), equalTo(41))
}

test.nextMethodWithMissingFirstArg <- function() {
  g.default <- function(x = 42) x 
  g.foo <- function(x = explode()) NextMethod() 
  g <- function(x) UseMethod('g') 
  x<-1
  class(x) <- 'foo';
  assertThat(g(), equalTo(42))
}


test.nextMethodWithMissingArg <- function() {
  
  g.default <- function(x,...) nargs() 
  g.foo <- function(x,i,j) NextMethod() 
  g <- function(x,i,j) UseMethod('g') 
  x<-1
  class(x) <- 'foo'
  assertThat(g(x,1), equalTo(2))
  assertThat(g(x,1,2), equalTo(3))
}


test.subsetWithinUseMethod <- function() {
  f.foo <- function(x, filter) { 
    e <- substitute(filter)
    l <- list(a=42,b=3)
    eval(e, l, NULL)
  }
  f <- function(x, filter) UseMethod('f') 
  x <- 1
  class(x) <- 'foo'
  assertThat(f(x, a+b), equalTo(45))
}


test.funCallInClosure <- function() {
  fn <- function(x) x 
  f <- function(fn) fn(16) 
  g <- function(fn) f(fn) 
  h <- sqrt
  assertThat(g(h), equalTo(4))
}


test.missingArgMasksFunction <- function() {
  f <- function(c) c() 
  assertThat(f(), throws.error)
}

test.correctEnclosingEnvironment <- function() {
  
  f<-function() { zz <- 42; local({ zz }) }
  
  assertThat(f(), equalTo(42))
}

test.doCallCall <- function() {
  x <- call('function.that.does.not.exist', 'foo')
}

test.evalWithNumericEnv <- function() {
  f <- function() {
  	eval(quote(var.in.calling.env), envir=-1L)
  }
  	 
  environment(f) <- new.env() 
  var.in.calling.env <- 42
  assertThat( f(), equalTo(42))
}

test.evalWithNumericNegEnv <- function() {
  f <- function() eval(quote(x), envir=-2L) 
  g <- function() { x<- 43; f() }
  assertThat( g(), equalTo(43))
}

test.callEvaluatesArguments <- function() {
  assertThat( call("list", 1+1), identicalTo(quote(list(2))) ) 
}