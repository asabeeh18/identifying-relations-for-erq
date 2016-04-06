
import java.util.Arrays;
import java.util.Scanner;

public class Trian {

    public static void main(String[] args) {
        Scanner r=new Scanner(System.in);
        int t=r.nextInt();
        while(t-->0)
        {
            int n=r.nextInt();
            int a[][]=new int[n+1][n+1];
            int box[][]=new int[n+2][n+2];
            for(int i=1;i<=n;i++)
            {
                for(int j=1;j<=i;j++)
                {
                    a[i][j]=r.nextInt();
                }
            }
            for(int i=n;i>0;i--)
            {
                for(int j=1;j<=i;j++)
                {
                    if(box[i+1][j]>box[i+1][j+1])
                    {
                        box[i][j]=a[i][j]+box[i+1][j];
                    }
                    else
                    {
                        box[i][j]=a[i][j]+box[i+1][j];
                    }
                }
                System.out.println(Arrays.deepToString(box));
            }
        }
    }
    
}
