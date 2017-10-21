"""
Searches module defines all different search algorithms
"""

def bfs(graph, initial_node, dest_node):
    """
    Breadth First Search
    uses graph to do search from the initial_node to dest_node
    returns a list of actions going from the initial node to dest_node
    """
    found=[]
    found.append(initial_node)
    addVertex=[]
    addVertex.append(initial_node)

    
    
    # Creating Dictonary of Distances and starPredeccsor 
    starPredeccsor={}
    distance = {}
    # Calculating distance and predecossr of initial Node
    distance[initial_node] =0
    starPredeccsor[initial_node] = None

    

    while (len(found)>0):
        currentVertex = found.pop(0)
        
        for nbr in graph.neighbors(currentVertex):
            
            if nbr not in addVertex:
                
                found.append(nbr)
                distance[nbr] = distance[currentVertex]+ graph.distance(currentVertex,nbr)
                starPredeccsor[nbr]= currentVertex
                addVertex.append(nbr)
                
        if dest_node in addVertex:
            #print("I breaked")
            break

            




    list =[]

    while starPredeccsor[dest_node] is not None:
        list = [graph.get_edge(starPredeccsor[dest_node], dest_node)] + list
        print (list)
        dest_node= starPredeccsor[dest_node]




return list

def dfs(graph, initial_node, dest_node):
    """
    Depth First Search
    uses graph to do search from the initial_node to dest_node
    returns a list of actions going from the initial node to dest_node
    """
    pass

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