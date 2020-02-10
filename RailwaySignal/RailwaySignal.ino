String inp;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  pinMode(22,OUTPUT);
  pinMode(23,OUTPUT);  
  pinMode(34,OUTPUT);
  pinMode(26,OUTPUT);
  pinMode(27,OUTPUT);
  pinMode(28,OUTPUT);
  pinMode(30,OUTPUT);
  pinMode(31,OUTPUT);
  pinMode(32,OUTPUT);
  pinMode(34,OUTPUT);
  pinMode(35,OUTPUT);
  pinMode(36,OUTPUT);
  pinMode(38,OUTPUT);
  pinMode(39,OUTPUT);
  pinMode(40,OUTPUT);
  pinMode(42,OUTPUT);
  pinMode(43,OUTPUT);
  pinMode(44,OUTPUT);
  pinMode(46,OUTPUT);
  pinMode(47,OUTPUT);
  pinMode(48,OUTPUT);
  pinMode(50,OUTPUT);
  pinMode(51,OUTPUT);
  pinMode(52,OUTPUT);


  digitalWrite(22,HIGH);
  digitalWrite(26,HIGH);
  digitalWrite(30,HIGH);
  digitalWrite(34,HIGH);
  digitalWrite(38,HIGH);
  digitalWrite(42,HIGH);
  digitalWrite(46,HIGH);
  digitalWrite(50,HIGH);

}

void loop() {
  // put your main code here, to run repeatedly:
   if(Serial.available()>0)
  {
    //char data = Serial.read();
    int i=0;
    while(Serial.available() && i<8)
    {
      char data= Serial.read();
      
      inp+=data;
      i++;
      
      
    }
    digitalWrite(22,LOW);
    digitalWrite(23,LOW);
    digitalWrite(24,LOW);
    digitalWrite(26,LOW);
    digitalWrite(27,LOW);
    digitalWrite(28,LOW);
    digitalWrite(30,LOW);
    digitalWrite(31,LOW);
    digitalWrite(32,LOW);
    digitalWrite(34,LOW);
    digitalWrite(35,LOW);
    digitalWrite(36,LOW);
    digitalWrite(38,LOW);
    digitalWrite(39,LOW);
    digitalWrite(40,LOW);
    digitalWrite(42,LOW);
    digitalWrite(43,LOW);
    digitalWrite(44,LOW);
    digitalWrite(46,LOW);
    digitalWrite(47,LOW);
    digitalWrite(48,LOW);
    digitalWrite(50,LOW);
    digitalWrite(51,LOW);
    digitalWrite(52,LOW);


    //left Outter outside signal
    if(inp[0] == 'Y')
    {
      digitalWrite(23,HIGH);
    }
     else if(inp[0] == 'G')
     {
        digitalWrite(24,HIGH);
     }
     else 
     {
         digitalWrite(22,HIGH);
     }



    //left Outter inside signal
     if(inp[1] == 'Y')
    {
      digitalWrite(27,HIGH);
    }
     else if(inp[1] == 'G')
     {
        digitalWrite(28,HIGH);
     }
     else 
     {
         digitalWrite(26,HIGH);
     }



    //left Inner outside signal
     if(inp[2] == 'Y')
    {
      digitalWrite(31,HIGH);
    }
     else if(inp[2] == 'G')
     {
        digitalWrite(32,HIGH);
     }
     else 
     {
         digitalWrite(30,HIGH);
     }



      //left Inner inside signal
     if(inp[3] == 'Y')
    {
      digitalWrite(35,HIGH);
    }
     else if(inp[3] == 'G')
     {
        digitalWrite(36,HIGH);
     }
     else 
     {
         digitalWrite(34,HIGH);
     }

     

     //right Inner inside signal 
     if(inp[4] == 'Y')
    {
      digitalWrite(39,HIGH);
    }
     else if(inp[4] == 'G')
     {
        digitalWrite(40,HIGH);
     }
     else 
     {
         digitalWrite(38,HIGH);
     }


     
     //right Inner Outside signal 
     if(inp[5] == 'Y')
    {
      digitalWrite(43,HIGH);
    }
     else if(inp[5] == 'G')
     {
        digitalWrite(44,HIGH);
     }
     else 
     {
         digitalWrite(42,HIGH);
     }


     
      //right Outter inside signal 
     if(inp[6] == 'Y')
    {
      digitalWrite(47,HIGH);
    }
     else if(inp[6] == 'G')
     {
        digitalWrite(48,HIGH);
     }
     else 
     {
         digitalWrite(46,HIGH);
     }


      //right Outter outside signal 
     if(inp[7] == 'Y')
    {
      digitalWrite(51,HIGH);
    }
     else if(inp[7] == 'G')
     {
        digitalWrite(52,HIGH);
     }
     else 
     {
         digitalWrite(50,HIGH);
     }
     
     
     Serial.println(inp);
     inp="";
  }
  delay(100);

}
