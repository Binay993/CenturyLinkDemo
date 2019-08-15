package com.centurylink.findmyfollowers.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.centurylink.findmyfollowers.models.Model;

@Component
public class BusinessService {
	
	private int startLevel;
	private int maxLevel;
	private int sizePerLevel;

	private RemoteService remoteService;

	public BusinessService(@Autowired RemoteService remoteService) {
		
		this.remoteService = remoteService;
	}

	public List<Model> process(String id, int startLevel, int maxLevel, int sizePerLevel) {

		this.startLevel = startLevel;
		this.maxLevel = maxLevel;
		this.sizePerLevel = sizePerLevel;
		return this.process(id, startLevel);
	}

	private List<Model> process(String id, int level) {

		if (level > maxLevel)
			return null;

		List<Model> retVal = new ArrayList<Model>();
		Model[] apiResp = remoteService.get(id);

		if (apiResp != null) {
			int counter = 0;

			for (Model m : apiResp) {

				if (counter < sizePerLevel) {
					m.setId(m.getLogin());
					m.setLevel(level);
					retVal.add(m);
				} else {
					break;
				}

				counter++;
			}
		}

		if (retVal.size() > 0) {
			parallelSearch(retVal);
		}

		return retVal;
	}

	private Model findModelForId(List<Model> models, String id) {

		return models.stream().filter(f -> f.getLogin().equals(id)).findFirst().get();
	}

	private void parallelSearch(List<Model> models) {

		List<Callable<Map<String, List<Model>>>> ccList = new ArrayList<Callable<Map<String, List<Model>>>>();

		models.forEach(model -> {

			Callable<Map<String, List<Model>>> c = () -> {
				Map<String, List<Model>> fMap = new HashMap<>();
				int nextLevel = model.getLevel() + 1;
				List<Model> resp = this.process(model.getLogin(), nextLevel);
				fMap.put(model.getLogin(), resp);
				return fMap;
			};

			ccList.add(c);
		});

		if (ccList.size() == 0)
			return;

		ExecutorService THREAD_POOLS = Executors.newFixedThreadPool(sizePerLevel);
		List<Future<Map<String, List<Model>>>> futures = null;

		try {
			long startProcessingTime = System.currentTimeMillis();
			futures = THREAD_POOLS.invokeAll(ccList);
			long totalProcessingTime = System.currentTimeMillis() - startProcessingTime;
			System.out.println("Processing time : " + Thread.currentThread().getName() + " : " + String.valueOf(totalProcessingTime) );
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}

		for (Future<Map<String, List<Model>>> futureResp : futures) {

			if (futureResp.isDone()) {

				try {

					Map<String, List<Model>> followersMap = futureResp.get();

					if (followersMap != null) {
						String id = followersMap.keySet().iterator().next();

						if (!StringUtils.isEmpty(id)) {
							Model parent = findModelForId(models, id);							
							if (parent != null) {
								parent.setFollowers(followersMap.get(id));
							}
						}
					}

				} catch (InterruptedException e) {
				} catch (ExecutionException e) {
				}
			}
		}

		if (!THREAD_POOLS.isShutdown()) {
			THREAD_POOLS.shutdown();
		}

	}
}
