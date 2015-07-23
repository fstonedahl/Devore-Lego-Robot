#!/usr/bin/env python3

from pylab import *

#~ def stripBothEnds(xArray,yArray, thresholdb):
	#~ global rightPts, leftPts, missingLeftPts, missingRightPts
	#~ 
	#~ firstGoodIndex = 0
	#~ for i in range(len(xArray)):
		#~ if yArray[i] < threshold:
			#~ firstGoodIndex = i
			#~ break
	#~ lastGoodIndex = 
	#~ for i in reversed(range(len(xArray))):
		#~ if yArray[i] < threshold:
			#~ lastGoodIndex = i
			#~ break
	

def main():
	global rightPts, leftPts, missingLeftPts, missingRightPts
	with open("range_data.txt") as fin:
		rightPts = []
		leftPts = []
		missingLeftPts = []
		missingRightPts = []
		allXPts = []
		allYPts = []
		
		hitsLists = []
		for caseNum in range(16):
			fin.readline() # blank line
			distLineStr = fin.readline().strip()
			directionLineStr = fin.readline().strip()
			distanceDataStr = fin.readline().strip()
			
			distLine = float(distLineStr.split(":")[1]) # in cm
			direction = directionLineStr.split(":")[1]
			distanceData = [float(x) for x in distanceDataStr.split(" ")]
			threshold = distLine / 100 + 1.2  # in meters
			
			if direction == "right":
				distanceData = list(reversed(distanceData))
			
			xStep = 2.0 / (len(distanceData) - 1)
			
			xArray = arange(len(distanceData)) * xStep - 1.0
			yArray = array(distanceData)
			
			#stripBothEnds(xArray,yArray, thresholdb)
			
			#~ figure()
			#~ plot(xArray,yArray, "bo")
			#~ title("Distance %s , direction %s"%(distLine, direction))
			#~ xlim(-1, 1)
			#~ ylim(0, 2.6)
			#~ savefig("vision_cone_dist%s_%s.png"%(distLine, direction), dpi=300)
			hitsLists.append([])
			for x,y in zip(xArray, yArray):
				if y > threshold:
					hitsLists[-1].append(0)
					#~ y = y + 2
					#~ if isinf(y):
						#~ y = 3
					y = distLine / 100
						
					if direction == "right":
						missingRightPts.append((x,y))
					else:
						missingLeftPts.append((x,y))
				else:
					hitsLists[-1].append(1)
					y = distLine / 100
					if direction == "right":
						rightPts.append((x,y))
					else:
						leftPts.append((x,y))
				allXPts.append(x)
				allYPts.append(y)
					
			print()
			print(distLine)
			print(direction)
			print(len(distanceData))
	
	fullHitsProbList =  []
	for i in range(len(hitsLists)):
		# take a rolling average with window size 10
		fullHitsProbList.extend(convolve(array(hitsLists[i]),ones((10,))/10, mode='same'))

	with open("vision_cone_probabilities.txt", "w") as fout:
		for trip in zip(allXPts, allYPts, fullHitsProbList):
			fout.write("%s %s %s\n"%trip)
		
	figure(figsize=(11,8.5))
	scatter(*zip(*leftPts), c='b', marker='|', alpha=1, s=15, label="L hit")
	#scatter(*zip(*rightPts), c='r', marker='|', alpha=1, s=15, label="R hit")
	scatter(*zip(*missingLeftPts), c='c', marker='|', alpha=1, s=15, label="L miss")
	#scatter(*zip(*missingRightPts), c='k', marker='|',  alpha=1, s=15, label="R miss")
	xlim(-0.75,.75)
	ylim(0,2.5)
	title ("Robot vision cone...")
	savefig("vision_cone_combined_left2.png", dpi=300)
	show()
	
	with open("vision_cone_hits.txt", "w") as fout:
		for pair in leftPts + rightPts:
			fout.write("%s %s\n"%pair)
	with open("vision_cone_misses.txt", "w") as fout:
		for pair in missingLeftPts + missingRightPts:
			fout.write("%s %s\n"%pair)
	
	
	
main()			
			
