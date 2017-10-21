"""
Searches module defines all different search algorithms
"""


def bfs(graph, initial_node, dest_node):

    arrived =[]
    arrived.append(initial_node)
    addVertex=[]    
    
    starPredecesor={}
    distance = {}

    distance[initial_node] =0
    starPredecesor[initial_node] = None
    addVertex.append(initial_node)



    while (len(arrived)>0):
        currentVertex = arrived[0]

        flag=True


        G = graph.neighbors(currentVertex)
        
    


        for nbr in  graph.neighbors(currentVertex): 
            if nbr not in addVertex:
                flag =False
                            
                arrived = [nbr] + arrived

                distance[nbr] = distance[currentVertex]+ graph.distance(currentVertex,nbr)
                print ("start printing Distace 4"); print(" ") ;  print(distance); print("end"); print("  ")
                starPredecesor[nbr]= currentVertex
                addVertex.append(nbr)
                break
        if dest_node in addVertex:
            break
        if flag:
            arrived.pop(0)
            print("sassasa") 



"""
pass
"""


    list =[]

    while starPredecesor[dest_node] is not None:
        list = [graph.get_edge(starPredecesor[dest_node], dest_node)] + list
        dest_node= starPredecesor[dest_node]

    return list

def dfs(graph, initial_node, dest_node):
    """
    Depth First Search
    uses graph to do search from the initial_node to dest_node
    returns a list of actions going from the initial node to dest_node
    """
    arrived =[]
    arrived.append(initial_node)
    addVertex=[]    

    starPredecesor={}
    distance = {}

    distance[initial_node] =0
    starPredecesor[initial_node] = None
    addVertex.append(initial_node)



    while (len(arrived)>0):
        currentVertex = arrived[0]

        flag=True


        G = graph.neighbors(currentVertex)
        
    


        for nbr in  graph.neighbors(currentVertex): 
            if nbr not in addVertex:
                flag =False
             
                
                
                arrived = [nbr] + arrived

                distance[nbr] = distance[currentVertex]+ graph.distance(currentVertex,nbr)
                print ("start printing Distace 4"); print(" ") ;  print(distance); print("end"); print("  ")
                starPredecesor[nbr]= currentVertex
                addVertex.append(nbr)
              
                break
        if dest_node in addVertex:
            break
        if flag:
            arrived.pop(0)
            print("sassasa") 



"""
pass
"""



    list =[]

    while starPredecesor[dest_node] is not None:
        list = [graph.get_edge(starPredecesor[dest_node], dest_node)] + list
        dest_node= starPredecesor[dest_node]




    return list



def dijkstra_search(graph, initial_node, dest_node):
    """
    Dijkstra Search
    uses graph to do search from the initial_node to dest_node
    returns a list of actions going from the initial node to dest_node
    """

    pass

def a_star_search(graph, initial_node, dest_node):
    """
    A* Search
    uses graph to do search from the initial_node to dest_node
    returns a list of actions going from the initial node to dest_node
    """

    pass