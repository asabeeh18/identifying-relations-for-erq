package Spitter;

public class DataReader
{
    StringBuilder[] raw, object, predicate, dub;
    int numPredi, numDub;
	
    public void separateStrings()
    {
        object = new StringBuilder[numPredi];
        predicate = new StringBuilder[numPredi];

	int j = 0;
	StringBuilder str = new StringBuilder("");
		
	for(int i = 0; i < numPredi; i++)
	{
            while(raw[i].charAt(j) != '=')
		str.append(raw[i].charAt(j++));
            
            predicate[i] = str;
            predicate[i] = new StringBuilder(predicate[i].toString().trim());
            predicate[i].insert(0, "|");
            predicate[i].append("|");
            str = new StringBuilder("");
            j++;
            
            while(j < raw[i].length())
		str.append(raw[i].charAt(j++));
            
            object[i] = str;
            object[i] = new StringBuilder(object[i].toString().trim());
            object[i].insert(0, "|");
            object[i].append("|");
            str = new StringBuilder("");
            j = 0;
	}
    }
    
    public void extractRawStrings(StringBuilder infobox)
    {
        raw = new StringBuilder[numPredi];
        dub = new StringBuilder[numDub];
	
	StringBuilder str = new StringBuilder(""), brack = new StringBuilder(""), pred;
	int i = 0, j = 0, k = 0, l;
	boolean or = false;
        
	while(i < infobox.length())
	{
            if(infobox.charAt(i) == '|')
            {
		i++;
		
		do
		{
                    if(infobox.charAt(i) == '[')
                    {
			i += 2;
			
			while(infobox.charAt(i) != ']')
			{
                            if(infobox.charAt(i) == '|')
                            {
				brack.append(infobox.charAt(i++));
				or = true;
                            }
                            else
				brack.append(infobox.charAt(i++));
			}
                        
                        i += 2;
			
			if(or)
			{
                            str.append("|");
                            str.append(brack.toString());
                            str.append("|");
			}
			else
                            str.append(brack);
                    }
                    else if(infobox.charAt(i) != '|')
			str.append(infobox.charAt(i++));
                }
		while(infobox.charAt(i) != '|' && infobox.charAt(i) != '}');
                
		if(or)
                    dub[k++] = str;
		else
                    raw[j++] = str;
		
		or = false;
                str = new StringBuilder("");
		brack = new StringBuilder("");
		i--;
            }
            
            i++;
	}
        
	for(i = 0; i < numDub; i++)
	{
            pred = new StringBuilder("");
            l = 0;
            
            while(dub[i].charAt(l) != '|')
                pred.append(dub[i].charAt(l++));
            
            l++;
            
            raw[j] = new StringBuilder(pred);
            raw[j + 1] = new StringBuilder(pred);
            
            while(dub[i].charAt(l) != '|')
		raw[j].append(dub[i].charAt(l++));
            
            l++;
            
            while(dub[i].charAt(l) != '|')
		raw[j + 1].append(dub[i].charAt(l++));
            
            l++;
            
            while(l < dub[i].length())
            {
                raw[j].append(dub[i].charAt(l));
		raw[j + 1].append(dub[i].charAt(l));
		
		l++;
            }
            
            j += 2;
        }
    }
	
    public Node[] readInfobox(String subject, String iBox)
    {
        StringBuilder infobox = new StringBuilder(iBox);
        numPredi = 0;
	numDub = 0;
        
        for(int i = 0; i < infobox.length(); i++)
            if(infobox.charAt(i) == '|')
                numPredi++;
            else if(infobox.charAt(i) == '=')
		numDub++;
        
	numDub = numPredi - numDub;

	extractRawStrings(infobox);
	separateStrings();
        
        Node[] arrNodes = new Node[numPredi];
        for(int i = 0; i < numPredi; i++)
            arrNodes [i] = new Node(subject, predicate[i].toString(), object[i].toString());
        return arrNodes;
    }
}