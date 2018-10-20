
# Create difference file for all results
with open("nating-all.txt", "r") as nating_all_file:
	with open("seamus-all.txt", "r") as seamus_all_file:
		with open("results-all.txt","w") as results_file:
			nating_all = nating_all_file.readlines()
			seamus_all = seamus_all_file.readlines()
			print("\ntrec_eval results:\n")
			print("%-*s\t%-*s\t%-*s\t%s\n" % (20,"Metric:",10,"Geoff's:",10,"Seamus':","Difference:"))
			for i, res in enumerate(nating_all):
				if i > 0:
					metric_name = res.split()[0]
					nating_res = round(float(res.split()[2]),3)
					seamus_res = round(float(seamus_all[i].split()[2]),3)
					diff = round(nating_res - seamus_res,3);
					results_file.write("%-*s\t%-*s\t%-*s\t%s\n" % (20,metric_name,10,nating_res,10,seamus_res,diff))
					print("%-*s\t%-*s\t%-*s\t%s" % (20,metric_name,10,nating_res,10,seamus_res,diff))