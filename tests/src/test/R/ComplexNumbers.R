
# from ComplexNumberSpec.html
test.complexNumbers <- function() {
  assertThat(Re(eigen(matrix(c(3, 4, -2, -1),2))$vectors[1]), closeTo(0.4082483,0.00001))
  assertThat(Im(as.complex(1)), identicalTo(0))
  assertThat(Re(as.complex(1)), identicalTo(1))
  assertThat(Mod(1+1i), closeTo(1.4142136,0.00001))
  assertThat(Im(1+1i + 1+3i), identicalTo(4.0))
  assertThat(Im((1+1i) - (1+3i)), identicalTo(-2.0))
  assertThat(Im(1+1i * 1+3i), identicalTo(4.0))
  assertThat(Re((1+1i) * (1+3i)), identicalTo(-2.0))
}

test.imaginaryPartOfConvertedDoubleShouldBeZero <- function() {
  assertThat(Im(as.complex(1)), equalTo(c(0)) )
}


test.realPartOfConvertedDoubleShouldMatch <- function() {
  assertThat(Re(as.complex(1)),equalTo(c(1)))
  assertThat(Re(as.complex(2)),equalTo(c(2)))
}


test.vectorizedReal <- function() {
  sqrt(c(1,4,9))
  assertThat(Re(c(as.complex(1),as.complex(2)))[1],equalTo(c(1)))
  assertThat(Re(c(as.complex(1),as.complex(2)))[2],equalTo(c(2)))
}


test.sizeAt0_1 <- function() {
  assertThat(Mod(complex(0,1)),equalTo(c(1)))
  assertThat(Mod(complex(0,9)),equalTo(c(9)))
}


test.argumentAt0_1 <- function() {
  assertThat(Arg(complex(real=0, i=1))/pi,equalTo(c(0.5)))
}


test.polarCoordinatesAt1_0 <- function() {
  assertThat(Mod(complex(real=1,i=0)), equalTo(c(1)))
  assertThat(Mod(complex(real=9,i=0)), equalTo(c(9)))
}


test.complexConjugate <- function() {
  assertThat(Im(Conj(complex(real=0,i=1))), equalTo(c(-1)))
}
