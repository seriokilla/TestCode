using System;
using System.Collections.Generic;
using System.Diagnostics.Eventing.Reader;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Mvc;

namespace SearchApi.Controllers
{
	public class SearchProvider
	{
		public string Name { get; set; }
		public int ID { get; set; }
		public string Description { get; set; }
		public DateTime CreateDateTime { get; set; }
	}

    public class SearchProviderController : ApiController
    {
	    private static List<SearchProvider> _SearchProviders = new List<SearchProvider>()
		    {
				new SearchProvider
				{
					Name = "Unknown",
					ID = -1,
					Description = "Unknown SearchProvider",
					CreateDateTime = DateTime.Now
				},
			    new SearchProvider
			    {
				    Name = "Google",
				    ID = 1,
				    Description = "Google AdWords",
				    CreateDateTime = DateTime.Now
			    },
			    new SearchProvider
			    {
				    Name = "Yahoo",
				    ID = 2,
				    Description = "Yahoo Search",
				    CreateDateTime = DateTime.Now
			    },
			    new SearchProvider
			    {
				    Name = "Bing",
				    ID = 70,
				    Description = "Microsoft Bing",
				    CreateDateTime = DateTime.Now
			    },
			    new SearchProvider
			    {
				    Name = "Gemini",
				    ID = 917,
				    Description = "Yahoo Gemini",
				    CreateDateTime = DateTime.Now
			    }
		    };

	    public SearchProvider[] GetAllSearchProviders()
	    {
		    return _SearchProviders.ToArray();
	    }

		public SearchProvider GetSearchProvider(int id)
		{
			var found = GetAllSearchProviders().Where(f => f.ID == id);
			return found.ToArray()[0];
		}

		public HttpResponseMessage Post(SearchProvider s)
		{
			s.CreateDateTime = DateTime.Now;
		    _SearchProviders.Add(s);
			var response = Request.CreateResponse(HttpStatusCode.Created, s);

			return response;
	    }


    }
}
