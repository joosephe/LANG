package Internals;

public class Transformation extends ComplexMatrix {
//public class Transformation {
	
	
	final static Transformation HAD= new Transformation("had");
	final static Transformation FLIP= new Transformation("flip");

	
	
	

	public Transformation(Complex[][] a) {
		
		super(a);
		if (!isUnitary(this)){
			try {
				throw new Exception("Transformation matrix must be unitary");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// TODO but now Transformation is still initialized. is this bad?
	}
	
	public Transformation (String a){
		Complex overSqrtTwo=new Complex((float) Math.sqrt(0.5),0);
		Complex overMinusSqrtTwo=new Complex((float) 0-Math.sqrt(0.5),0);
		Complex one=new Complex(1,0);
		Complex zero= new Complex(0,0);
		switch(a){
			case "had":{
				Complex[] over = new Complex[2];
				over[0]=overSqrtTwo;
				over[1]=overSqrtTwo;
				Complex[] under = new Complex[2];
				under[0]=overSqrtTwo;
				under[1]=overMinusSqrtTwo;
				Complex[][] ha = new Complex[2][2];
				ha[0]=over;
				ha[1]=under;
				this.elems=ha;
				this.rowCount=2;
				this.colCount=2;
			}
			case "flip":{
				Complex[] over = new Complex[2];
				over[0]=zero;
				over[1]=one;
				Complex[] under = new Complex[2];
				under[0]=one;
				under[1]=zero;
				Complex[][] ha = new Complex[2][2];
				ha[0]=over;
				ha[1]=under;
				this.elems=ha;
				this.rowCount=2;
				this.colCount=2;
			}
			
				
		}
	}
	
	public Transformation(int n) {
		//does unit transformation.
		this.rowCount=n;
		this.colCount=n;
		Complex[][] mats= new Complex[n][n];
		Complex one =new Complex(1,0);
		Complex zero =new Complex(0,0);
		for(int i=0;i<rowCount;i++){
			//System.out.println("i= " +i);

			for(int j=0;j<colCount;j++){
				//System.out.println("j= " +j);

				if(i!=j){
					mats[i][j]=zero;
				}
				else{
					mats[i][j]=one;
				}
			}
		}
		this.elems=mats;
	}
	
	public Transformation(ComplexMatrix a) {
		this.rowCount=a.elems.length;
		this.colCount=a.elems[0].length;
			
		Complex[][] mats= new Complex[rowCount][colCount];
			//can I just copy a matrix?
		this.elems=mats;
			//can I just copy a matrix?
		for(int i=0;i<rowCount;i++){
			for(int j=0;j<colCount;j++){
				this.elems[i][j]=a.elems[i][j];
			}
		}
			
		if (!isUnitary(this)){
			try {
				throw new Exception("Transformation matrix must be unitary");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// TODO but now Transformation is still initialized. is this bad?
	}
	
	//yes, we access the private values of the Qubit qb, but that qubit can not be used afterwards anyway.
	public static Qubit apply(Qubit qb, Transformation tr){
		//we transform them both into matrices.
		ComplexMatrix vec = new ComplexMatrix(2,1);
		vec.elems[0][0]=qb.noPart;
		vec.elems[1][0]=qb.yesPart;
		//we multiply the transformation with the qubit  
		ComplexMatrix result = multiply(tr, vec);
		qb.noPart=result.elems[0][0];
		qb.yesPart=result.elems[1][0];
		
		return qb;
	}

	
	
	public static QuantumState apply(QuantumState st, Transformation tr){
		//we transform them both into matrices.
		//ComplexMatrix mat= tr;
		ComplexMatrix vec = new ComplexMatrix(st.stateVector.length,1);
		for(int i = 0;i<st.stateVector.length;i++){
			vec.elems[i][0]=st.stateVector[i];
		}
		ComplexMatrix result = multiply(tr, vec);
		//ComplexMatrix resVec = new ComplexMatrix(st.stateVector.length,1);

		for(int i = 0;i<st.stateVector.length;i++){
			//resVec.stateVector[i]=result.elems[i][0];
			st.stateVector[i]=result.elems[i][0];
		}
		
		return st;
	}
	
	
	//this sort of destroys the values of this ensemble maybe? whatever.
	public static Ensemble apply(Ensemble en, Transformation tr){
		for(int i=0;i<en.size;i++){
			en.states[i]=apply(en.states[i],tr);
		}
		return en;
		
	}
	
	
	public static Transformation tensorProd (Transformation first, Transformation second){
		int newRow=first.rowCount*second.rowCount;
		int newCol=first.colCount*second.colCount;
		Complex[][] bigmat = new Complex[newRow][newCol];
		
		
		for(int i=0;i<first.rowCount; i++){
			for(int j=0;j<second.rowCount; j++){
				for(int k=0;k<first.colCount; k++){
					for(int l=0;l<second.colCount; l++){
						//bigmat[i*second.rowCount+j][k*second.colCount+l]=first.elems[]
						//result.elems[i*second.rowCount+j][k*second.colCount+l]=Complex.Multiply(first.elems[i][k],second.elems[j][l]);
						bigmat[i*second.rowCount+j][k*second.colCount+l]=Complex.Multiply(first.elems[i][k],second.elems[j][l]);

					}
				}
			}
		}
		
		Transformation result = new Transformation(bigmat);
		//TODO: write tensor product
		return result;
	}
	
	

	//public Complex [][] elems;
	//the size of
	//public int size;
	
	
	
	
	
	
}
