

run.suites <- function(path = getwd()) {
  suites <- list.files(path, pattern="*Test.R")
  for(suite in suites) {
    cat(sprintf("SUITE: %s\n", suite))
    cat("===============\n")
    defenv <- new.env()
    eval(quote(source(file=file.path(path, suite), local=TRUE)), envir=defenv)
    run.tests(env=defenv)
  }
} 

run.tests <- function(env = .GlobalEnv) {
  tests <- grep(pattern="^test\\.", ls(envir=env), value=TRUE)
  num.passed <- 0
  for(test in tests) {
    passed <- tryCatch({
      eval(call(test), envir=env)
      num.passed <- num.passed + 1
      cat(sprintf("%s: OK\n", test))
      TRUE
    }, error = function(e) {
      cat(sprintf("%s: FAILED\n", test))
      cat(sprintf("%s\n\n", e$message))
    })
    
  }
  cat(sprintf("PASSED %d/%d\n", num.passed, length(tests) ))
}




# from the hamcrest package
# copied here for 


assertTrue <- function(value) {
  if(!value) {
    stop(paste("\nExpected: !", deparse(substitute(value))))
  }
}

assertFalse <- function(value) {
  if(value) {
    stop(paste("\nExpected: !", deparse(substitute(value))))
  }
}

assertThat <- function(actual, matcher) {
  if(!matcher(actual)) {
    stop(paste("\nExpected:", deparse(substitute(matcher)), "\ngot: ", paste(deparse(actual), collapse="\n" )))
  }
}


closeTo <- function(expected, delta) {
  function(actual) {
    length(expected) == length(actual) &&
      all(abs(expected-actual)<delta)	
  }
}

identicalTo <- function(expected) {
  function(actual) {
    identical(expected, actual)
  }
}

equalTo <- function(expected) {
  function(actual) {
    length(actual) == length(expected) &&
      actual == expected
  }
}

throws.error <- function(x) {
  tryCatch({ 
    x 
    cat("x evaled")
    return(FALSE)
  }, error = function(e) {
    return(TRUE)
  })
}