
      subroutine test(x,n)
      integer n
      double precision x(n,n)

      integer i, j

      do 100 i=1,n
          x(i,i) = i
  100 continue

      return
      end
