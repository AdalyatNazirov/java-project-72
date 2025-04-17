@import hexlet.code.dto.urls.UrlsPage
@import hexlet.code.util.NamedRoutes

@param UrlsPage page

@template.layout.main(
title = "Sites",
page = page,
content = @`
    <h1>Managed Websites</h1>

    <div class="mt-4">
        @if(page.getUrls().isEmpty())
            <div class="alert alert-info">No sites available</div>
        @else
            <table class="table table-striped table-hover">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Website Name</th>
                    <th>Last visit</th>
                    <th>Status code</th>
                </tr>
                </thead>
                <tbody>
                @for(var url : page.getUrls())
                    <tr>
                        <td>${url.getId()}</td>
                        <td>
                            <a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a>
                        </td>
                        <td>
                            @if(page.getChecks().containsKey(url.getId()))
                                ${page.getChecks().get(url.getId()).getFormattedCreatedAt()}
                            @endif
                        </td>
                        <td>
                            @if(page.getChecks().containsKey(url.getId()))
                                ${page.getChecks().get(url.getId()).getStatusCode()}
                            @endif
                        </td>
                    </tr>
                @endfor
                </tbody>
            </table>
        @endif

        <div class="mt-3">
            <a href="${NamedRoutes.rootPath()}" class="btn btn-primary">Track New Website</a>
        </div>
    </div>
`
)
