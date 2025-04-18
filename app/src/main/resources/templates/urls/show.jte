@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.util.NamedRoutes
@param UrlPage page

@template.layout.main(
title = "URL Details",
content = @`
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>Website Analytics</h1>
        <div>
            <a href="/urls" class="btn btn-outline-secondary">Return to Dashboard</a>
        </div>
    </div>

    <div class="card mb-4">
        <div class="card-header">
            <h5 class="card-title mb-0">Website Profile</h5>
        </div>
        <div class="card-body">
            <table class="table table-bordered mb-0">
                <tr>
                    <th style="width: 200px">ID</th>
                    <td>${page.getUrl().getId()}</td>
                </tr>
                <tr>
                    <th>Website</th>
                    <td><a href="${page.getUrl().getName()}">${page.getUrl().getName()}</a></td>
                </tr>
                <tr>
                    <th>Added On</th>
                    <td>${page.getUrl().getFormattedCreatedAt()}</td>
                </tr>
            </table>
        </div>
    </div>

    <h2>Checks</h2>

    <form action="${NamedRoutes.urlCheckPath(page.getUrl().getId())}" method="post" class="mb-4">
        <button type="submit" class="btn btn-primary">Check Now</button>
    </form>

    @if(page.getUrlChecks().isEmpty())
        <div class="alert alert-info">No checks performed yet</div>
    @else
        <table class="table table-striped table-hover">
            <thead>
            <tr>
                <th>ID</th>
                <th>Status Code</th>
                <th>Title</th>
                <th>H1</th>
                <th>Description</th>
                <th>Created At</th>
            </tr>
            </thead>
            <tbody>
            @for(var check : page.getUrlChecks())
                <tr>
                    <td>${check.getId()}</td>
                    <td>${check.getStatusCode()}</td>
                    <td>${check.getTitle()}</td>
                    <td>${check.getH1()}</td>
                    <td>${check.getDescription()}</td>
                    <td>${check.getFormattedCreatedAt()}</td>
                </tr>
            @endfor
            </tbody>
        </table>
    @endif
`
)
